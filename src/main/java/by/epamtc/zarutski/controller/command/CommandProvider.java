package by.epamtc.zarutski.controller.command;

import java.util.HashMap;
import java.util.Map;

import by.epamtc.zarutski.controller.command.impl.AuthenticationCommand;
import by.epamtc.zarutski.controller.command.impl.GoToAuthenticationCommand;
import by.epamtc.zarutski.controller.command.impl.GoToRegistrationCommand;
import by.epamtc.zarutski.controller.command.impl.LocalizationCommand;
import by.epamtc.zarutski.controller.command.impl.LogoutCommand;
import by.epamtc.zarutski.controller.command.impl.RegistrationCommand;
import by.epamtc.zarutski.controller.command.impl.GoToMainCommand;
import by.epamtc.zarutski.controller.command.impl.GoToPersonalAreaCommand;

public class CommandProvider {
	
	private Map<ParameterName, Command> commands = new HashMap<>();

    public CommandProvider() {
        commands.put(ParameterName.AUTHENTICATION, new AuthenticationCommand());
        commands.put(ParameterName.REGISTRATION, new RegistrationCommand());
        commands.put(ParameterName.LOGOUT, new LogoutCommand());
        commands.put(ParameterName.LOCALIZATION, new LocalizationCommand());
        commands.put(ParameterName.GO_TO_MAIN_PAGE, new GoToMainCommand());
        commands.put(ParameterName.GO_TO_REGISTRATION_PAGE, new GoToRegistrationCommand());
        commands.put(ParameterName.GO_TO_PERSONAL_AREA, new GoToPersonalAreaCommand());
        commands.put(ParameterName.GO_TO_AUTHENTICATION_PAGE, new GoToAuthenticationCommand());
    }

    public Command getCommand(String commandName) {
        Command command;
        ParameterName valueName;

        commandName = commandName.toUpperCase();
        valueName = ParameterName.valueOf(commandName);

        command = commands.get(valueName);

        return command;
    }
}