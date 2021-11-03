package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddToOrderCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EndAndTransactOrderCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListInventoryCommand;
import seedu.address.logic.commands.ListTransactionCommand;
import seedu.address.logic.commands.RemoveFromOrderCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.StartOrderCommand;

public class HelpCommandParserTest {

    private HelpCommandParser parser = new HelpCommandParser();

    @Test
    public void parse_validAddArgs_returnsHelpCommand() {
        // asking help for add command
        final String message = AddCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "add", expectedHelpCommand);
    }

    @Test
    public void parse_validEditArgs_returnsHelpCommand() {
        // asking help for edit command
        final String message = EditCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "edit", expectedHelpCommand);
    }

    @Test
    public void parse_validDeleteArgs_returnsHelpCommand() {
        // asking help for delete command
        final String message = DeleteCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "delete", expectedHelpCommand);
    }

    @Test
    public void parse_validClearArgs_returnsHelpCommand() {
        // asking help for clear command
        final String message = ClearCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "clear", expectedHelpCommand);
    }

    @Test
    public void parse_validFindArgs_returnsHelpCommand() {
        // asking help for find command
        final String message = FindCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "find", expectedHelpCommand);
    }

    @Test
    public void parse_validSortArgs_returnsHelpCommand() {
        // asking help for sort command
        final String message = SortCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "sort", expectedHelpCommand);
    }

    @Test
    public void parse_validExitArgs_returnsHelpCommand() {
        // asking help for exit command
        final String message = ExitCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "exit", expectedHelpCommand);
    }

    @Test
    public void parse_validStartOrderArgs_returnsHelpCommand() {
        // asking help for start order command
        final String message = StartOrderCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "sorder", expectedHelpCommand);
    }

    @Test
    public void parse_validAddToOrderArgs_returnsHelpCommand() {
        // asking help for add to command
        final String message = AddToOrderCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "iorder", expectedHelpCommand);
    }

    @Test
    public void parse_validRemoveFromOrderArgs_returnsHelpCommand() {
        // asking help for remove from order command
        final String message = RemoveFromOrderCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "corder", expectedHelpCommand);
    }

    @Test
    public void parse_validEndAndTransactionOrderArgs_returnsHelpCommand() {
        // asking help for end and transaction command
        final String message = EndAndTransactOrderCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(message);
        assertParseSuccess(parser, "eorder", expectedHelpCommand);
    }

    @Test
    public void parse_validListsArgs_returnsHelpCommand() {
        // asking help for view order command
        String messageUsage = ListInventoryCommand.MESSAGE_USAGE + "\n"
                + ListTransactionCommand.MESSAGE_USAGE;
        HelpCommand expectedHelpCommand = new HelpCommand(messageUsage);
        assertParseSuccess(parser, "list", expectedHelpCommand);
    }

    @Test
    public void parse_validEmptyArgs_success() {
        // asking help for empty command
        HelpCommand expectedHelpCommand = new HelpCommand(HelpCommand.DEFAULT_MESSAGE.substring(1));
        assertParseSuccess(parser, "", expectedHelpCommand);
    }



}
