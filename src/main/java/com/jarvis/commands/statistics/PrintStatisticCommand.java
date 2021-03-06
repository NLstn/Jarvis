package com.jarvis.commands.statistics;

import com.jarvis.commands.Command;
import com.jarvis.module.ModuleHandler;
import com.jarvis.module.modules.command.CommandDomain;

public class PrintStatisticCommand extends Command {

    public PrintStatisticCommand() {
        super(CommandDomain.STATISTICS, new String[] { "print", "printStatistic", "printStat" });
    }

    @Override
    public void execute() {
        int value = ModuleHandler.getStatisticsModule().getStatisticsValue(args[0]);
        logger.info("Statistic: " + args[0] + ", Value: " + value);
    }

    @Override
    public boolean validateArguments() {
        return args.length == 1;
    }

}