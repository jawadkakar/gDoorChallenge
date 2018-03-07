package com.gdoor.cli;

import org.apache.commons.cli.*;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Cli {

    private static final String VERBOSE_OPTION = "verbose";
    private static final String FILE_OPTION = "file";

    /**
     * "Definition" stage of command-line parsing with Apache Commons CLI.
     *
     * @return Definition of command-line options.
     */
    protected static Options generateOptions() {
        final Option verboseOption = Option.builder("v")
                .required(false)
                .hasArg(false)
                .longOpt(VERBOSE_OPTION)
                .desc("Print status with verbosity.")
                .build();
        final Option fileOption = Option.builder("f")
                .required()
                .longOpt(FILE_OPTION)
                .hasArg()
                .desc("File to be processed.")
                .build();
        final Options options = new Options();
        options.addOption(verboseOption);
        options.addOption(fileOption);
        return options;
    }

    /**
     * "Parsing" stage of command-line processing demonstrated with
     * Apache Commons CLI.
     *
     * @param options              Options from "definition" stage.
     * @param commandLineArguments Command-line arguments provided to application.
     * @return Instance of CommandLine as parsed from the provided Options and
     * command line arguments; may be {@code null} if there is an exception
     * encountered while attempting to parse the command line options.
     */
    protected static CommandLine generateCommandLine(
            final Options options, final String[] commandLineArguments) {
        final CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = cmdLineParser.parse(options, commandLineArguments);
        } catch (ParseException parseException) {
            System.out.println(
                    "ERROR: Unable to parse command-line arguments "
                            + Arrays.toString(commandLineArguments) + " due to: "
                            + parseException);
        }
        return commandLine;
    }

    /**
     * Generate usage information with Apache Commons CLI.
     *
     * @param options Instance of Options to be used to prepare
     *                usage formatter.
     * @return HelpFormatter instance that can be used to print
     * usage information.
     */
    protected static void printUsage(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        final String syntax = "java -jar Cli.jar -f <path to datasource yaml file>";
        System.out.println("\n=====");
        System.out.println("USAGE");
        System.out.println("=====");
        final PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        formatter.printUsage(pw, 80, syntax, options);
        pw.flush();
    }

    /**
     * Generate help information with Apache Commons CLI.
     *
     * @param options Instance of Options to be used to prepare
     *                help formatter.
     * @return HelpFormatter instance that can be used to print
     * help information.
     */
    protected static void printHelp(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        final String syntax = "java -jar Cli.jar -f <path to datasource yaml file>";
        final String usageHeader = "";//"Looking Glass Code Challenge for new hire.";
        final String usageFooter = "";//"See http://marxsoftware.blogspot.com/ for further details.";
        System.out.println("\n====");
        System.out.println("HELP");
        System.out.println("====");
        formatter.printHelp(syntax, usageHeader, options, usageFooter);
    }

    public static String getVerboseOption() {
        return VERBOSE_OPTION;
    }

    public static String getFileOption() {
        return FILE_OPTION;
    }
}
