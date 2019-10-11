package com.tyaathome.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.tyaathome.annotation.NetPack;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class NetPackProcessor extends AbstractProcessor {

    private static final String PACKAGE_NAME = "com.pcs.knowing_weather";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(NetPack.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        TypeSpec.Builder classSpecBuilder = TypeSpec.classBuilder("NetFactory")
                .addModifiers(Modifier.PUBLIC);

        ClassName returnType = ClassName.get("com.pcs.knowing_weather.net.pack.base", "BasePackDown");
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("getResponse")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String.class, "keytemp")
                .returns(returnType)
                .addStatement("if (android.text.TextUtils.isEmpty(keytemp)) return null")
                .addStatement("$T[] keyList = keytemp.split(\"#\")", String.class)
                .addStatement("if (keyList.length == 0) return null")
                .addStatement("$T key = keyList[0]", String.class)
                .beginControlFlow("switch (key)");

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(NetPack.class);
        for(Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            TypeName packUpType = TypeName.get(typeElement.asType());
            methodSpecBuilder.addCode("case $T.NAME:\n", packUpType);

            // 获取带参数的父类
            ParameterizedTypeName superClass = (ParameterizedTypeName) ParameterizedTypeName.get(typeElement.getSuperclass());
            List<TypeName> typeArguments = superClass.typeArguments;
            if(typeArguments != null && typeArguments.size() == 1) {
                TypeName argument = typeArguments.get(0);
                methodSpecBuilder.addStatement("$>return new $T()$<", argument);
            } else {
                methodSpecBuilder.addStatement("$>return null$<");
            }
        }
        methodSpecBuilder.endControlFlow();
        methodSpecBuilder.addStatement("return null");

        TypeSpec classSpec = classSpecBuilder.addMethod(methodSpecBuilder.build()).build();
        JavaFile javaFile = JavaFile.builder(PACKAGE_NAME, classSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getPackageName(TypeElement type) {
        return processingEnv.getElementUtils().getPackageOf(type).getQualifiedName().toString();
    }

//    private MethodSpec generateMethodCode(TypeElement typeElement) {
//        TypeMirror superClass = typeElement.getSuperclass();
//        ParameterizedTypeName name1 = (ParameterizedTypeName) ParameterizedTypeName.get(superClass);
//        List<TypeName> typeArguments = name1.typeArguments;
//
//        String str = superClass.toString();
//        //ClassName name = ClassName.get("com.pcs.knowing_weather.net.pack.base", "BasePackDown");
//        String fullName = getPackageName(typeElement);
//        String name = typeElement.getSimpleName().toString();
//        ClassName className = ClassName.get(fullName, name);
//        className.topLevelClassName();
//        ClassName superName = ClassName.get("com.pcs.knowing_weather.net.pack.base", "BasePackUp");
//        ClassName param = ClassName.get(fullName, "PackInitDown");
//        TypeName returnClass = ParameterizedTypeName.get(superName, param);
//
//        MethodSpec methodSpec = MethodSpec.methodBuilder("getResponse")
//                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
//                .addCode("return new " + typeArguments.get(0).toString() + "();")
//                .returns(typeArguments.get(0)).build();
//        return methodSpec;
//    }
}
