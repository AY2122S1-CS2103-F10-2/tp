package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.item.Item;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_ID + "ID "
            + PREFIX_COUNT + "COUNT "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Banana Bread "
            + PREFIX_ID + "#019381 "
            + PREFIX_COUNT + "10 "
            + PREFIX_TAG + "baked "
            + PREFIX_TAG + "popular";

    public static final String MESSAGE_SUCCESS = "New item added: %1$s";
    public static final String MESSAGE_SUCCESS_REPLENISH = "Item replenished: %1$s";
    public static final String MESSAGE_DUPLICATE_ITEM = "This item already exists in the inventory";

    private final Item toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Item item) {
        requireNonNull(item);
        toAdd = item;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasItem(toAdd)) {
            // throw new CommandException(MESSAGE_DUPLICATE_ITEM);
            Item inInventory = model.getItemWithName(toAdd.getName().toString());
            toAdd.replenishItem(inInventory.getCount());
            // TODO: HASN'T ACCOUNTED IF ID IS DIFF
            model.setItem(inInventory, toAdd);

            return new CommandResult(String.format(MESSAGE_SUCCESS_REPLENISH, toAdd));
        }
        model.addItem(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
