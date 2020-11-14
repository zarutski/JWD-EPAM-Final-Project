package by.epamtc.zarutski.controller.command;

import java.util.HashMap;
import java.util.Map;

import by.epamtc.zarutski.controller.command.impl.*;
import by.epamtc.zarutski.controller.command.impl.go_to.*;
import by.epamtc.zarutski.controller.command.impl.admin.*;
import by.epamtc.zarutski.controller.command.impl.user.*;

/**
 * The class {@code CommandProvider} that provides access to the objects
 * of Command's {@code Command} interface implementations
 *
 * @author Maksim Zarutski
 */
public class CommandProvider {

    /**
     * Map object contains the command name corresponding command implementation object
     */
    private final Map<ParameterName, Command> commands = new HashMap<>();

    /**
     * Filling map object with command implementations
     */
    public CommandProvider() {
        // common commands
        commands.put(ParameterName.LOGOUT, new LogoutCommand());
        commands.put(ParameterName.LOCALIZATION, new LocalizationCommand());
        commands.put(ParameterName.GO_TO_ABOUT_US_PAGE, new GoToAboutUsCommand());
        commands.put(ParameterName.GO_TO_CONTACTS_PAGE, new GoToContactsCommand());

        commands.put(ParameterName.REGISTRATION, new RegistrationCommand());
        commands.put(ParameterName.AUTHENTICATION, new AuthenticationCommand());
        commands.put(ParameterName.GO_TO_REGISTRATION_PAGE, new GoToRegistrationCommand());
        commands.put(ParameterName.GO_TO_AUTHENTICATION_PAGE, new GoToAuthenticationCommand());

        // authenticated user commands
        commands.put(ParameterName.GO_TO_MAIN_PAGE, new GoToMainCommand());
        commands.put(ParameterName.GO_TO_PERSONAL_AREA, new GoToPersonalAreaCommand());
        commands.put(ParameterName.USER_PHOTO_UPLOAD, new UserPhotoUploadCommand());
        commands.put(ParameterName.UPDATE_USER_DATA, new UpdateUserDataCommand());
        commands.put(ParameterName.GO_TO_CARD_DETAILS, new GoToCardDetailsCommand());
        commands.put(ParameterName.BLOCK_CARD, new BlockCardCommand());
        commands.put(ParameterName.GO_TO_ACCOUNT_DETAILS, new GoToAccountDetailsCommand());

        // only user commands
        commands.put(ParameterName.GO_TO_CARDS, new GoToCardsCommand());
        commands.put(ParameterName.GO_TO_CARD_ORDER, new GoToCardOrderCommand());
        commands.put(ParameterName.ORDER_CARD, new OrderCardCommand());
        commands.put(ParameterName.GO_TO_PAYMENTS, new GoToPaymentsCommand());
        commands.put(ParameterName.TRANSFER, new TransferCommand());
        commands.put(ParameterName.GO_TO_ACCOUNTS, new GoToAccountsCommand());

        // only admin commands
        commands.put(ParameterName.GO_TO_SEARCH_USER, new GoToSearchUserCommand());
        commands.put(ParameterName.SEARCH_USER, new SearchUserCommand());
        commands.put(ParameterName.GO_TO_USER_DETAILS, new GoToUserDetailsCommand());
        commands.put(ParameterName.CHANGE_USER_ROLE, new ChangeUserRoleCommand());
        commands.put(ParameterName.UNBLOCK_CARD, new UnblockCardCommand());
    }

    /**
     * Finds object of command's implementation by command name
     *
     * @param commandName name of the command
     * @return object of the command implementation
     */
    public Command getCommand(String commandName) {
        Command command;
        ParameterName valueName;

        commandName = commandName.toUpperCase();
        valueName = ParameterName.valueOf(commandName);

        command = commands.get(valueName);

        return command;
    }
}