package com.jarvis.commands.settings;

import java.util.List;

import com.jarvis.commands.Command;
import com.jarvis.module.ModuleHandler;
import com.jarvis.module.modules.command.CommandDomain;
import com.jarvis.module.modules.settings.Setting;

public class PrintSettingCommand extends Command {

	public PrintSettingCommand() {
		super(CommandDomain.SETTINGS, new String[] { "print", "printSetting" });
	}

	public void execute() {
		if (args.length == 0 || args[0].equals("all")) {
			List<Setting> settings = ModuleHandler.getSettingsModule().getSettings();
			for (Setting setting : settings) {
				logger.info("Setting " + setting.getKey() + " has value " + setting.getValue());
			}
		} else {
			String value = ModuleHandler.getSettingsModule().getSetting(args[0]);
			if (value == null)
				logger.info("Setting not found!");
			else
				logger.info("Setting " + args[0] + " has value " + value);
		}
	}

	@Override
	public boolean validateArguments() {
		return args.length <= 1;
	}

}
