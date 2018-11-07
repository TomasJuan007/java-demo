package com.example.demo.cli;

import org.apache.commons.cli.*;

public abstract class AbstractCmdProcessor {

    protected String cmdLineSyntax;

    public void process(String[] args){
        Options options = getOptions();

        Option optionH = new Option("h","help", true, "show usage information");
        optionH.setOptionalArg(true);
        optionH.setArgName(null);
        options.addOption(optionH);

        CommandLineParser cliParser = new DefaultParser();
        try {
            CommandLine line =  cliParser.parse(options, args);

            if(line.getOptions().length == 0 || line.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.setWidth(200);
                formatter.printHelp(cmdLineSyntax, "available options", getUsageOptions(options), "");
                return;
            }

            processOptions(line);

        } catch (UnrecognizedOptionException e){
            String invalidOption = e.getOption();
            String firstLine = String.format("Invalid option -- '%s'", invalidOption);
            String secondLine = "Try '-h' for more information.";
            System.out.println(firstLine);
            System.out.println(secondLine);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    protected abstract Options getUsageOptions(Options options);

    protected abstract void processOptions(CommandLine cmdLine);

    protected abstract Options getOptions();
}
