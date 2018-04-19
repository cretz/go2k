package main

import (
	"encoding/json"
	"fmt"
	"go/ast"
	"go/parser"
	"go/token"
	"os"
	"reflect"
	"strings"
)

func main() {
	if err := run(); err != nil {
		fmt.Fprintf(os.Stderr, "error: %v\n", err)
		os.Exit(1)
	}
}

func run() error {
	if len(os.Args) != 2 {
		return fmt.Errorf("Expected single arg of dir path")
	}
	// Parse the dir (ignore pos for now)
	fileSet := token.NewFileSet()
	pkgs, err := parser.ParseDir(fileSet, os.Args[1], nil, parser.ParseComments)
	if err != nil {
		return err
	}
	// Remove some circular refs
	for _, pkg := range pkgs {
		for _, file := range pkg.Files {
			ast.Inspect(file, func(node ast.Node) bool {
				switch n := node.(type) {
				case *ast.Ident:
					if n.Obj != nil {
						n.Obj.Decl = nil
					}
				}
				return true
			})
			file.Scope = nil
		}
	}
	jsonable := interfaceToJsonable(map[string]interface{}{"packages": pkgs})
	byts, err := json.MarshalIndent(jsonable, "", " ")
	if err != nil {
		return err
	}
	_, err = os.Stdout.Write(byts)
	return err
}

func interfaceToJsonable(iface interface{}) interface{} {
	if iface == nil {
		return nil
	}
	// Grab the val and type
	reflectVal := reflect.ValueOf(iface)
	reflectType := reflectVal.Type()
	if reflectType.Kind() == reflect.Ptr {
		reflectType = reflectType.Elem()
		reflectVal = reflectVal.Elem()
	}
	switch reflectType.Kind() {
	case reflect.Array, reflect.Slice:
		ret := make([]interface{}, reflectVal.Len())
		for i := 0; i < len(ret); i++ {
			ret[i] = interfaceToJsonable(reflectVal.Index(i).Interface())
		}
		return ret
	case reflect.Map:
		ret := map[string]interface{}{}
		for _, keyVal := range reflectVal.MapKeys() {
			ret[fmt.Sprintf("%v", keyVal.Interface())] = interfaceToJsonable(reflectVal.MapIndex(keyVal).Interface())
		}
		return ret
	case reflect.Struct:
		if !reflectVal.IsValid() {
			return iface
		}
		ret := map[string]interface{}{"_type": reflectType.Name()}
		// Put all fields into the map
		for i := 0; i < reflectType.NumField(); i++ {
			fieldVal := reflectVal.Field(i)
			if !fieldVal.IsValid() {
				continue
			}
			fieldName := reflectType.Field(i).Name
			fieldName = strings.ToLower(fieldName[:1]) + fieldName[1:]
			val := fieldVal.Interface()
			if val != nil {
				val = interfaceToJsonable(val)
			}
			if val != nil {
				ret[fieldName] = interfaceToJsonable(val)
			}
		}
		return ret
	default:
		return iface
	}
}
