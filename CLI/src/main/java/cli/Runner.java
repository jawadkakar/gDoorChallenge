package cli;

import com.gdoor.ingester.module.Ingester;
import com.gdoor.ingester.module.IngesterModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import static cli.Cli.*;

public class Runner {



 /*   public static void main(String[] args) {
        Options options = Cli.generateOptions();
        CommandLine commandLine = Cli.generateCommandLine(options, args);
        final String fileName = commandLine.getOptionValue(FILE_OPTION);
        delegateToIngester(fileName);
    }*/

    private static void delegateToIngester(String fileName) {
        Injector ingesterInjector = Guice.createInjector(new IngesterModule());
        Ingester ingester = ingesterInjector.getInstance(Ingester.class);
        ingester.ingest(fileName);
    }


    public static void main(String[] args) {
        Options options = generateOptions();
        CommandLine commandLine = generateCommandLine(options, args);
        String fileName = null;
        if (commandLine == null) {

            printHelp(options);
            printUsage(options);
        } else {
            commandLine.hasOption(getVerboseOption());
            fileName = commandLine.getOptionValue(getFileOption());

        }
        delegateToIngester(fileName);
    }


}
