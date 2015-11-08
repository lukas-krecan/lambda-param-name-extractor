# Extracts parameter names from Java 8 lambdas

Extracts parameter name from lambda. Inspired by Benji Weber's [code](https://github.com/benjiman/lambda-type-references/blob/master/src/main/java/com/benjiweber/typeref/MethodFinder.java)

## Usage:
    import net.javacrumbs.lambdaextractor.ParameterNameExtractor

    ...
    SerializableFunction lambda = name -> "a";
    ParameterNameExtractor.extractParameterNames(lambda); // returns "name"

    ...
    SerializableBiFunction lambda = (name1, name2) -> "a";
    ParameterNameExtractor.extractParameterNames(lambda); // returns name1, name2
    ParameterNameExtractor.extractFirstParameterName(lambda); // return name1
