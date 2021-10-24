package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.DisplayMode.DISPLAY_INVENTORY;

import seedu.address.commons.core.Messages;
import seedu.address.model.Model;
import seedu.address.model.item.IdContainsNumberPredicate;
import seedu.address.model.item.NameContainsKeywordsPredicate;

/**
 * Finds and lists all items in inventory whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all items whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_ID + "ID "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Banana Bread or "
            + PREFIX_ID + "019381 ";

    private final NameContainsKeywordsPredicate namePredicate;
    private final IdContainsNumberPredicate idPredicate;

    /**
     * Creates FindCommand in the case of query by name
     *
     * @param namePredicate name of the item that the user is finding
     */
    public FindCommand(NameContainsKeywordsPredicate namePredicate) {
        this.namePredicate = namePredicate;
        this.idPredicate = null;
    }

    /**
     * Creates FindCommand in the case of query by id
     *
     * @param idPredicate id of the item that the user is finding
     */
    public FindCommand(IdContainsNumberPredicate idPredicate) {
        this.idPredicate = idPredicate;
        this.namePredicate = null;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (namePredicate == null) {
            model.updateFilteredItemList(DISPLAY_INVENTORY, idPredicate);
        } else {
            model.updateFilteredItemList(DISPLAY_INVENTORY, namePredicate);
        }
        return new CommandResult(
                String.format(Messages.MESSAGE_ITEMS_LISTED_OVERVIEW, model.getFilteredItemList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (namePredicate == null) {
            return other == this // short circuit if same object
                    || (other instanceof FindCommand // instanceof handles nulls
                    && idPredicate.equals(((FindCommand) other).idPredicate)); // state check
        } else {
            return other == this // short circuit if same object
                    || (other instanceof FindCommand // instanceof handles nulls
                    && namePredicate.equals(((FindCommand) other).namePredicate)); // state check
        }
    }

}
