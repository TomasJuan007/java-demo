package com.example.demo.cli;

import com.example.demo.util.EncryptionUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Collection;

public class EncryptionCmdProcessor extends AbstractCmdProcessor {
    
    private final static String PLAIN_TEXT_OPT = "p";
    
    private final static String PLAIN_TEXT_OPT_LONG = "plaintext";
    
    private final static String CIPHER_TEXT_OPT= "c";
    
    private final static String CIPHER_TEXT_OPT_LONG = "ciphertext";


    public EncryptionCmdProcessor() {
        this.cmdLineSyntax = "(Deprecated)encrypt.sh [OPTION] [TEXT]\n";
    }

    @Override
    protected void processOptions(CommandLine cmdLine) {
        if (cmdLine.hasOption(PLAIN_TEXT_OPT)) {
            String ciphertext = EncryptionUtils.encode(cmdLine.getOptionValue(PLAIN_TEXT_OPT).trim());
            System.out.println("Cipher Text: [" + ciphertext + "]");
        }
        
        if (cmdLine.hasOption(CIPHER_TEXT_OPT)) {
            String plaintext = EncryptionUtils.decode(cmdLine.getOptionValue(CIPHER_TEXT_OPT).trim());
            System.out.println("Plain Text: [" + plaintext + "]");
        }
    }

    @Override
    protected Options getOptions() {
        Options options = new Options();
        Option optionP = new Option(PLAIN_TEXT_OPT,PLAIN_TEXT_OPT_LONG, true, "plain text to be encrypted");
        optionP.setArgName("plain text");
        
        Option optionC = new Option(CIPHER_TEXT_OPT,CIPHER_TEXT_OPT_LONG, true, "cipher text to be decrypted");
        optionC.setArgName("cipher text");
        
        options.addOption(optionC);
        options.addOption(optionP);
        return options;
    }

    @Override
    protected Options getUsageOptions(Options options) {
        Options usageOptions = new Options();
        Collection<Option> optionCollection = options.getOptions();
        
        for(Option op : optionCollection){
            if(!op.getOpt().equals(CIPHER_TEXT_OPT)){
                usageOptions.addOption(op);
            }
        }
        return usageOptions;
    }

}
