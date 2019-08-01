package com.example.demo.cli;

import com.example.demo.util.DestroyUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Collection;

public class DestroyCmdProcessor extends AbstractCmdProcessor {

    private final static String MODE_OPT = "m";

    private final static String MODE_OPT_LONG = "mode";

    private final static String FILEPATH_OPT= "fp";

    private final static String FILEPATH_OPT_LONG = "filepath";

    private final static String DEPTH_OPT= "d";

    private final static String DEPTH_OPT_LONG = "depth";

    private final static String END_WITH_OPT= "ew";

    private final static String END_WITH_OPT_LONG = "endwith";


    public DestroyCmdProcessor() {
        this.cmdLineSyntax = "e.g. processor.process(new String[]{\"-m\", \"Transverse\", \"-fp\", \"C:\\Users\\ETOMHUA\\Desktop\\dump\"})\n" +
                "[Attention!] This util will destroy files, be careful to use.";
    }

    @Override
    protected void processOptions(CommandLine cmdLine) {
        String mode = cmdLine.getOptionValue(MODE_OPT);
        String filepath = cmdLine.getOptionValue(FILEPATH_OPT);
        String depth = cmdLine.getOptionValue(DEPTH_OPT, "1");
        int depthInt = Integer.parseInt(depth);
        String endWith = cmdLine.getOptionValue(END_WITH_OPT);

        DestroyUtil.destroy(mode, filepath, depthInt, endWith);
    }

    @Override
    protected Options getOptions() {
        Options options = new Options();
        Option optionM = Option.builder(MODE_OPT).longOpt(MODE_OPT_LONG).numberOfArgs(1)
                .desc("mode, [PathWalk, Transverse]").build();

        Option optionFp = Option.builder(FILEPATH_OPT).longOpt(FILEPATH_OPT_LONG).numberOfArgs(1)
                .desc("file path, e.g. C:\\Users\\ETOMHUA\\Desktop\\dump").build();

        Option optionD = Option.builder(DEPTH_OPT).longOpt(DEPTH_OPT_LONG).numberOfArgs(1)
                .desc("depth for PathWalk mode").build();

        Option optionEw = Option.builder(END_WITH_OPT).longOpt(END_WITH_OPT_LONG).numberOfArgs(1)
                .desc("endWith pattern for PathWalk mode").build();

        options.addOption(optionM);
        options.addOption(optionFp);
        options.addOption(optionD);
        options.addOption(optionEw);
        return options;
    }

    @Override
    protected Options getUsageOptions(Options options) {
        Options usageOptions = new Options();
        Collection<Option> optionCollection = options.getOptions();

        for(Option op : optionCollection){
            usageOptions.addOption(op);
        }
        return usageOptions;
    }

}
