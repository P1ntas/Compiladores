package pt.up.fe.comp2023;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.parser.JmmParserResult;
import pt.up.fe.comp2023.jmm.jasmin.ImplementedJasminBackend;
import pt.up.fe.comp2023.jmm.ollir.Optimizer;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;

public class Launcher {

    public static void main(String[] args) {
        // Setups console logging and other things
        SpecsSystem.programStandardInit();

        // Parse arguments as a map with predefined options
        var config = parseArgs(args);

        // Get input file
        File inputFile = new File(config.get("inputFile"));

        // Check if file exists
        if (!inputFile.isFile()) {
            throw new RuntimeException("Expected a path to an existing input file, got '" + inputFile + "'.");
        }

        // Read contents of input file
        String code = SpecsIo.read(inputFile);
        code = SpecsIo.read("test/pt/up/fe/comp/cpf/3_ollir/arrays/ComplexArrayAccess.jmm");

        // Instantiate JmmParser
        SimpleParser parser = new SimpleParser();

        // Parse stage
        JmmParserResult parserResult = parser.parse(code, config);
        parserResult.getReports();
        // Check if there are parsing errors\
        TestUtils.noErrors(parserResult.getReports());
        // ... add remaining stages
        Analysis analysis = new Analysis();
        analysis.semanticAnalysis(parserResult);

        JmmSemanticsResult semanticsResult = analysis.semanticAnalysis(parserResult);
        System.out.println(semanticsResult.getRootNode().toTree());
        TestUtils.noErrors(semanticsResult);

        Optimizer optimizer = new Optimizer();
        semanticsResult = optimizer.optimize(semanticsResult);

        TestUtils.noErrors(semanticsResult);

        OllirResult ollirResult = optimizer.toOllir(semanticsResult);

        TestUtils.noErrors(ollirResult);

        System.out.println("Execute Ollir code: \n");
        System.out.println(ollirResult.getOllirCode());

        ollirResult = optimizer.optimize(ollirResult);

        TestUtils.noErrors(ollirResult);


        ImplementedJasminBackend implementedJasminBackend = new ImplementedJasminBackend();
        JasminResult jasminResult = implementedJasminBackend.toJasmin(ollirResult);
        jasminResult.compile();
        System.out.println("Execute Jasmin code: \n");
        System.out.println(jasminResult.getJasminCode());
        jasminResult.run();
        TestUtils.noErrors(jasminResult);
    }

    private static Map<String, String> parseArgs(String[] args) {
        SpecsLogs.info("Executing with args: " + Arrays.toString(args));

        // Check if there is at least one argument
        if (args.length != 1) {
            throw new RuntimeException("Expected a single argument, a path to an existing input file.");
        }

        // Create config
        Map<String, String> config = new HashMap<>();
        config.put("inputFile", args[0]);
        config.put("optimize", "false");
        config.put("registerAllocation", "-1");
        config.put("debug", "false");

        return config;
    }

}
