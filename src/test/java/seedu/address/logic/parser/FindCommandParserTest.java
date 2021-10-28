package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_ID_LENGTH_AND_SIGN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_BAGEL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_100PLUS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BAGEL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_DONUT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_H20;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.item.IdContainsNumberPredicate;
import seedu.address.model.item.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validIdArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new IdContainsNumberPredicate(Arrays.asList("140262")));
        assertParseSuccess(parser, " id/140262", expectedFindCommand);
    }

    @Test
    public void parse_negativeIdArgs_throwsParseException() {
        assertParseFailure(parser, " id/-123123", String.format(
                MESSAGE_INVALID_ID_LENGTH_AND_SIGN, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_notSixDigitsIdArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new IdContainsNumberPredicate(Arrays.asList(VALID_ID_BAGEL)));
        assertParseSuccess(parser, " id/123", expectedFindCommand);
    }

    @Test
    public void parse_validNameArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(VALID_NAME_BAGEL, VALID_NAME_DONUT)));
        FindCommand expectedFindCommand2 =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Bagel donut")));
        assertParseSuccess(parser, " n/Bagel n/Donut", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " n/Bagel   n/Donut", expectedFindCommand);
        // names with spaces
        assertParseSuccess(parser, " n/Bagel donut", expectedFindCommand2);
    }

    @Test
    public void parse_validNameWithNumbersArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(VALID_NAME_100PLUS, VALID_NAME_H20)));
        assertParseSuccess(parser, " n/100Plus n/H20", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " n/100Plus     n/H20", expectedFindCommand);
    }

}
