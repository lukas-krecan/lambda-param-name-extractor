# Extracts parameter names from Java 8 lambdas

Extracts parameter name from lambda. Inspired by Benji Weber's [code](https://github.com/benjiman/lambda-type-references/blob/master/src/main/java/com/benjiweber/typeref/MethodFinder.java)

## Usage:
    import net.javacrumbs.lambdaextractor.ParameterNameExtractor

    ...
    // lambda type has to be serializable
    SerializableFunction lambda = name -> "a";
    ParameterNameExtractor.extractParameterNames(lambda); // returns "name"

    ...
    // lambda type has to be serializable
    SerializableBiFunction lambda = (name1, name2) -> "a";
    ParameterNameExtractor.extractParameterNames(lambda); // returns name1, name2
    ParameterNameExtractor.extractFirstParameterName(lambda); // return name1

## Maven dependency

    <dependency>
        <groupId>net.javacrumbs</groupId>
        <artifactId>lambda-param-name-extractor</artifactId>
        <version>0.1.0</version>
    </dependency>
