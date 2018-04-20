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
	jsonable := astToJsonable(fileSet, pkgs)
	byts, err := json.MarshalIndent(jsonable, "", "  ")
	if err != nil {
		return err
	}
	_, err = os.Stdout.Write(byts)
	return err
}

func astToJsonable(fileSet *token.FileSet, pkgs map[string]*ast.Package) interface{} {
	// Load up object decl refs and replace with ref map
	ctx := &jsonableCtx{refPtrs: map[interface{}]struct{}{}}
	for _, pkg := range pkgs {
		for _, file := range pkg.Files {
			ast.Inspect(file, func(node ast.Node) bool {
				switch n := node.(type) {
				case *ast.Ident:
					if n.Obj != nil && n.Obj.Decl != nil && reflect.TypeOf(n.Obj.Decl).Kind() == reflect.Ptr {
						ctx.refPtrs[n.Obj.Decl] = struct{}{}
						n.Obj.Decl = map[string]interface{}{
							"_type": "Ref",
							"_ref":  fmt.Sprint(reflect.ValueOf(n.Obj.Decl).Pointer()),
						}
					}
				}
				return true
			})
		}
	}
	return ctx.interfaceToJsonable(map[string]interface{}{"packages": pkgs})
}

type jsonableCtx struct {
	refPtrs map[interface{}]struct{}
}

func (c *jsonableCtx) interfaceToJsonable(iface interface{}) interface{} {
	if iface == nil {
		return nil
	}
	// Grab the val and type
	reflectVal := reflect.ValueOf(iface)
	reflectType := reflectVal.Type()
	var refUintPtr uintptr
	if reflectType.Kind() == reflect.Ptr {
		if _, ok := c.refPtrs[iface]; ok {
			refUintPtr = reflectVal.Pointer()
		}
		reflectType = reflectType.Elem()
		reflectVal = reflectVal.Elem()
	}
	switch reflectType.Kind() {
	case reflect.Array, reflect.Slice:
		ret := make([]interface{}, reflectVal.Len())
		for i := 0; i < len(ret); i++ {
			ret[i] = c.interfaceToJsonable(reflectVal.Index(i).Interface())
		}
		return ret
	case reflect.Map:
		ret := map[string]interface{}{}
		for _, keyVal := range reflectVal.MapKeys() {
			ret[fmt.Sprintf("%v", keyVal.Interface())] = c.interfaceToJsonable(reflectVal.MapIndex(keyVal).Interface())
		}
		return ret
	case reflect.Struct:
		if !reflectVal.IsValid() {
			return iface
		}
		ret := map[string]interface{}{"_type": reflectType.Name()}
		if refUintPtr > 0 {
			ret["_ptr"] = fmt.Sprint(refUintPtr)
		}
		// Put all fields into the map
		for i := 0; i < reflectType.NumField(); i++ {
			fieldVal := reflectVal.Field(i)
			if !fieldVal.IsValid() {
				continue
			}
			// Ignore nil struct/iface field vals
			if (fieldVal.Kind() == reflect.Ptr || fieldVal.Kind() == reflect.Interface) && fieldVal.IsNil() {
				continue
			}
			fieldName := reflectType.Field(i).Name
			fieldName = strings.ToLower(fieldName[:1]) + fieldName[1:]
			val := fieldVal.Interface()
			if val != nil {
				val = c.interfaceToJsonable(val)
			}
			if val != nil {
				ret[fieldName] = val
			}
		}
		return ret
	default:
		return iface
	}
}
