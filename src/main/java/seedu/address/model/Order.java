package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.item.Item;
import seedu.address.model.item.UniqueItemList;

public class Order {
    private final UniqueItemList items;

    /**
     * Instantiates an empty order.
     */
    public Order() {
        items = new UniqueItemList();
    }

    /**
     * Instantiates an order with a list of ordered items.
     */
    public Order(List<Item> itemCollection) {
        requireNonNull(itemCollection);
        items = new UniqueItemList();
        items.setItems(itemCollection);
    }

    /**
     * Add an {@code Item} to the order.
     */
    public void addItem(Item newItem) {
        // TODO: Implement count increasing to duplicate item adding.
        requireNonNull(newItem);
        items.add(newItem);
    }

    /**
     * Remove the specified {@code Item} from order.
     */
    public void removeItem(Item toBeRemoved) {
        requireNonNull(toBeRemoved);

        for (Item item : items.asUnmodifiableObservableList()) {
            if (item.isSameItem(toBeRemoved)) { // Same name OR same id
                items.remove(item);
                break;
            }
            ;
        }
    }

    /**
     * Gets a list of items in the order.
     */
    public ObservableList<Item> getOrderItems() {
        return items.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Order)) {
            return false;
        }

        Order otherOrder = (Order) other;
        return items.equals(otherOrder.items);
    }
}
