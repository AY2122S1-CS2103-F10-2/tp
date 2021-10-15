package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.item.Item;
import seedu.address.model.item.ItemDescriptor;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Item.
 */
public class ItemUtil {

    /**
     * Returns an add command string for adding the {@code item}.
     */
    public static String getAddCommand(ItemDescriptor itemDescriptor) {
        String arguments = getItemDescriptorDetails(itemDescriptor);

        return AddCommand.COMMAND_WORD + " " + arguments.substring(PREFIX_NAME.toString().length());
    }

    /**
     * Returns a delete command string for adding the {@code item}.
     */
    public static String getDeleteCommand(Item item) {
        return DeleteCommand.COMMAND_WORD
                + " " + item.getName()
                + " " + PREFIX_COUNT + item.getCount();
    }

    /**
     * Returns the part of command string for the given {@code item}'s details.
     */
    public static String getItemDetails(Item item) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + item.getName().fullName + " ");
        sb.append(PREFIX_ID + item.getId() + " ");
        sb.append(PREFIX_COUNT + Integer.toString(item.getCount()) + " ");
        item.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code ItemDescriptor}'s details.
     */
    public static String getItemDescriptorDetails(ItemDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getId().ifPresent(id -> sb.append(PREFIX_ID).append(id).append(" "));
        descriptor.getCount().ifPresent(count -> sb.append(PREFIX_COUNT).append(count).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
