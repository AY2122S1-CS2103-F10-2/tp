package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_BAGEL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_DONUT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BAGEL;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ITEMS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalItems.APPLE_PIE;
import static seedu.address.testutil.TypicalItems.BAGEL;
import static seedu.address.testutil.TypicalItems.BANANA_MUFFIN;
import static seedu.address.testutil.TypicalItems.DONUT;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.item.Item;
import seedu.address.model.item.ItemDescriptor;
import seedu.address.model.item.NameContainsKeywordsPredicate;
import seedu.address.testutil.InventoryBuilder;
import seedu.address.testutil.ItemDescriptorBuilder;
import seedu.address.testutil.TypicalItems;
import seedu.address.testutil.TypicalOrders;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new Inventory(), new Inventory(modelManager.getInventory()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setInventoryFilePath(Paths.get("inventory/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setInventoryFilePath(Paths.get("new/inventory/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setInventoryFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setInventoryFilePath(null));
    }

    @Test
    public void setInventoryFilePath_validPath_setsInventoryFilePath() {
        Path path = Paths.get("inventory/file/path");
        modelManager.setInventoryFilePath(path);
        assertEquals(path, modelManager.getInventoryFilePath());
    }

    @Test
    public void hasItem_nullItem_throwsNullPointerException() {
        //Search by item
        assertThrows(NullPointerException.class, () -> modelManager.hasItem((Item) null));
    }

    @Test
    public void hasItem_itemNotInInventory_returnsFalse() {
        assertFalse(modelManager.hasItem(APPLE_PIE));
    }

    @Test
    public void hasItem_itemInInventory_returnsTrue() {
        modelManager.addItem(APPLE_PIE);

        assertTrue(modelManager.hasItem(APPLE_PIE));
    }

    @Test
    public void getItem_itemInInventory_returnsItem() {
        modelManager.addItem(BAGEL);

        // Search by name
        ItemDescriptor descriptor = new ItemDescriptorBuilder().withName(VALID_NAME_BAGEL).build();
        assertEquals(modelManager.getItems(descriptor), List.of(BAGEL));

        // Search by id
        descriptor = new ItemDescriptorBuilder().withId(VALID_ID_BAGEL).build();
        assertEquals(modelManager.getItems(descriptor), List.of(BAGEL));

        // Search by name and id
        descriptor = new ItemDescriptorBuilder()
                .withName(VALID_NAME_BAGEL).withId(VALID_ID_BAGEL).build();
        assertEquals(modelManager.getItems(descriptor), List.of(BAGEL));
    }

    @Test
    public void getItem_itemNotInInventory_returnEmptyList() {
        modelManager.addItem(DONUT);

        // Search by name
        ItemDescriptor descriptor = new ItemDescriptorBuilder().withName(VALID_NAME_BAGEL).build();
        assertEquals(modelManager.getItems(descriptor), List.of());

        // Search by id
        descriptor = new ItemDescriptorBuilder().withId(VALID_ID_BAGEL).build();
        assertEquals(modelManager.getItems(descriptor), List.of());

        // Search by name and id
        descriptor = new ItemDescriptorBuilder()
                .withName(VALID_NAME_BAGEL).withId(VALID_ID_BAGEL).build();
        assertEquals(modelManager.getItems(descriptor), List.of());
    }

    @Test
    public void getItem_multipleMatches_returnMultiple() {
        modelManager.addItem(DONUT);
        modelManager.addItem(BAGEL);

        ItemDescriptor descriptor = new ItemDescriptorBuilder()
                .withName(VALID_NAME_BAGEL).withId(VALID_ID_DONUT).build();
        assertEquals(modelManager.getItems(descriptor), List.of(DONUT, BAGEL));
    }

    @Test
    public void getFilteredItemList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredItemList().remove(0));
    }

    @Test
    public void equals() {
        Inventory inventory = new InventoryBuilder().withItem(APPLE_PIE).withItem(BANANA_MUFFIN).build();
        Inventory differentInventory = new Inventory();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(inventory, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(inventory, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different inventory -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentInventory, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = APPLE_PIE.getName().fullName.split("\\s+");
        modelManager.updateFilteredItemList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(inventory, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredItemList(PREDICATE_SHOW_ALL_ITEMS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setInventoryFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(inventory, differentUserPrefs)));
    }

    // ========= order related methods tests ==========

    @Test
    public void setOrder_nullOrder_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setOrder(null));
    }

    @Test
    public void setOrder_emptyOrder_orderIsSet() {
        Order order = new Order();
        modelManager.setOrder(order);

        assertTrue(modelManager.hasUnclosedOrder());
    }

    @Test
    public void setOrder_typicalOrder_orderIsSet() {
        modelManager.setOrder(TypicalOrders.getTypicalOrder());

        assertEquals(modelManager.getOrder(), TypicalOrders.getTypicalOrder());
    }

    @Test
    public void addToOrder_nullItem_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.addToOrder(null));
    }

    @Test
    public void addToOrder_noOrderIsSetYet_throwAssertionError() {
        ModelManager model = new ModelManager();
        assertThrows(AssertionError.class, () -> model.addToOrder(APPLE_PIE));
    }

    @Test
    public void addToOrder_normalItem_itemAdded() {
        modelManager.setOrder(new Order());
        Order expectedOrder = new Order();
        modelManager.addToOrder(APPLE_PIE);
        expectedOrder.addItem(APPLE_PIE);

        assertEquals(modelManager.getOrder(), expectedOrder);
    }

    /*
    @Test
    public void addToOrder_duplicateItem_itemCountIncrease() {
        // TODO: Implement duplicate item count increasing!
        modelManager.setOrder(TypicalOrders.getTypicalOrder());
        Order expectedOrder = TypicalOrders.getTypicalOrder();
        modelManager.addToOrder(APPLE_PIE);
        expectedOrder.addItem(APPLE_PIE);

        assertEquals(modelManager.getOrder(), expectedOrder);
    }*/

    @Test
    public void removeFromOrder_nullItem_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.removeFromOrder(null));
    }

    @Test
    public void removeFromOrder_noOrderIsSetYet_throwAssertionError() {
        ModelManager model = new ModelManager();
        assertThrows(AssertionError.class, () -> model.removeFromOrder(APPLE_PIE));
    }

    @Test
    public void removeFromOrder_normalItem_itemRemoved() {
        modelManager.setOrder(TypicalOrders.getTypicalOrder());
        Order expectedOrder = TypicalOrders.getTypicalOrder();
        modelManager.removeFromOrder(APPLE_PIE);
        expectedOrder.removeItem(APPLE_PIE);

        assertEquals(modelManager.getOrder(), expectedOrder);
    }

    @Test
    public void removeFromOrder_nonExistingItem_orderNotChanged() {
        modelManager.setOrder(TypicalOrders.getTypicalOrder());
        Order expectedOrder = TypicalOrders.getTypicalOrder();
        modelManager.removeFromOrder(TypicalItems.getRandomItem());

        assertEquals(modelManager.getOrder(), expectedOrder);
    }

    @Test
    public void transactAndClearOrder_noOrderIsSetYet_throwAssertionError() {
        ModelManager model = new ModelManager();
        assertThrows(AssertionError.class, model::transactAndClearOrder);
    }

    @Test
    public void transactAndClearOrder_typicalOrder_inventoryIsClear() {
        ModelManager model = new ModelManager();
        model.setInventory(TypicalItems.getTypicalInventory());
        model.setOrder(TypicalOrders.getTypicalOrder());
        model.transactAndClearOrder();

        assertFalse(model.hasUnclosedOrder());
        assertEquals(model.getInventory(), new Inventory());
    }
}
