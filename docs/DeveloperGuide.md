---
layout: page
title: Developer Guide
parent: For Developers
nav_order: 1
---

* Table of Contents {:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the
  original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in
the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML
Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit
diagrams.
</div>

### Architecture

![Architecture Diagram](images/ArchitectureDiagram.png)

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes
called [`Main`](https://github.com/nus-cs2103-AY2122S1/tp/blob/master/src/main/java/seedu/address/Main.java)
and [`MainApp`](https://github.com/nus-cs2103-AY2122S1/tp/blob/master/src/main/java/seedu/address/MainApp.java). It
is responsible for,

* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues
the command `delete Milk -c 5`.

//TODO: modify `saveInventory()` later
![Delete Sequence Diagram](images/ArchitectureSequenceDiagram.png)

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding
  API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using
the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component
through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the
implementation of a component), as illustrated in the (partial) class diagram below.

![Component Managers](images/ComponentManagers.png)

The sections below give more details of each component.

### UI component

The **API** of this component is specified
in [`Ui.java`](https://github.com/AY2122S1-CS2103-F10-2/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`
, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures
the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that
are in the `src/main/resources/view` folder. For example, the layout of
the [`MainWindow`](https://github.com/AY2122S1-CS2103-F10-2/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java)
is specified
in [`MainWindow.fxml`](https://github.com/AY2122S1-CS2103-F10-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**
API** : [`Logic.java`](https://github.com/AY2122S1-CS2103-F10-2/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

![Logic class diagram](images/LogicClassDiagram.png)

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is
   executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add an item).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API
call.

![Interactions Inside the Logic Component for the `delete Milk -c 5` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:

* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a
  placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse
  the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as
  a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser`
  interface so that they can be treated similarly where possible e.g, during testing.

### Model component

**
API** : [`Model.java`](https://github.com/AY2122S1-CS2103-F10-2/tp/blob/master/src/main/java/seedu/address/model/Model.java)

![Model class diagram](images/ModelClassDiagram.png)

The `Model` component

- stores the inventory data i.e., all `Item` objects (which are contained in a `UniqueItemList` object).
- stores the current order data i.e., an `Order` which contains all `Items` added to it.
- stores the transaction history of orders i.e., a set of `TransactionRecord` objects.
- stores the currently 'selected' `Item` objects (e.g., results of a search query) 
  as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Item>`
  that can be 'observed' e.g. the UI can be bound to this list 
  so that the UI automatically updates when the data in the list change.
- stores a `UserPref` object that represents the user’s preferences. 
  This is exposed to the outside as a `ReadOnlyUserPref` objects.
- does not depend on any of the other three components (as the Model represents data entities of the domain,
  they should make sense on their own without depending on other components)
- it is in charge of internal interactions of `Item`, `Inventory`, `Order` and `TrasactionRecord` objects.
  i.e., updates `Inventory` when `Order` is placed by user, and keep the order histories as `TransactionRecord`.

### Storage component

**
API** : [`Storage.java`](https://github.com/AY2122S1-CS2103-F10-2/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,

* can save both inventory data and user preference data in json format, and read them back into corresponding
  objects.
* inherits from both `InventoryStorage` and `UserPrefStorage`, which means it can be treated as either one (if only
  the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects
  that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo
history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the
following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()`
and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the
initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command
calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes
to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book
state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`
, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing
the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer`
once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once
to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such
as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`.
Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not
pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be
purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern
desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


### Find feature 

The find mechanism is facilitated by the built-in `Predicate` class. The FindCommand constructor takes in a predicate 
type as a parameter and the list is then filtered with `ModelManager#updateFilteredItemList` method to only contain the 
items that match the predicate specified. `Predicate<Item>` interface is implemented by 3 different classes:

* `IdContainsNumberPredicate` — allows finding of items by Id
* `NameContainsKeywordsPredicate` — allows finding of items by Name
* `TagContainsKeywordsPredicate` — allows finding of items by Tag

Given below is an example usage scenario and how the find mechanism behaves at each step.

Step 1. The user opens up BogoBogo and executes `find n/Chocolate` to find items with the name chocolate. The 
`LogicManager` then calls the `AddressBookParser` to execute `FindCommandParser#parse()`. The `FindCommandParser` 
then creates a `FindCommand` with the `NameContainsKeywordsPredicate` as a field in its constructor. Then, the 
`LogicManager` calls the `FindCommand#execute()` which will update the filtered list with items that matches the 
predicate stated. 

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the user input a wrong format of the name, id or tag, a ParseException will be thrown by FindCommandParser and a FindCommand will not be created. 

Step 2. The updated list with items that matches the predicate will then be shown to the user. If none matches, an empty
list will be shown. The same procedure above is executed for finding by Id and Tags as well. 

<div markdown="span" class="alert alert-info">:information_source: **Note:** The FindCommand also supports finding by multiple names, ids or tags.
The 3 classes that implement Predicate<Item> takes in a list of string which allows storing of multiple predicates. 

<div>

The following sequence diagram shows how the find operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

The following activity diagram summarizes what happens when a user executes a Find command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How Find executes:**

* **Alternative 1:** Retrieve current inventory and check each item one by one without using the predicate class
    * Pros: Easier to implement
    * Cons: May have performance issue as every command will execute several loops. 


### Sort feature

The sort mechanism is facilitated by the built-in `Comparator` interface. The SortCommand constructor takes in a 
predicate enum instruction as a parameter depending on whether the user requested to sort by name or count. The 
items' respective fields are then compared with a `Comparator` so that the updated list displayed is sorted. 
`Comparator<Item>` interface is implemented by e different classes:

* `ItemNameComparator` — allows sorting of items by name
* `ItemCountComparator` — allows sorting of items by count

Given below is an example usage scenario and how the sort mechanism behaves at each step.

Step 1. The user opens up BogoBogo and executes `sort n/` to sort items by name. The `LogicManager` then calls the 
`AddressBookParser` to execute `SortCommandParser#parse()`. The `SortCommandParser` then creates a `SortCommand` with 
the enum `SortOrder.BY_NAME` as a field in its constructor. Then, the `LogicManager` calls the `SortCommand#execute()` 
which will call the `ModelManager#sortItems()` with the `ItemNameComparator` as its parameter. This will then update the 
display list with items sorted by name according to the `ItemNameComparator#compare()` method.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the user input does not input any field to sort by, SortCommandParser will throw a ParseException and a SortCommand will not be created.

Step 2. The updated list with items sorted by name will then be shown to the user. The same procedure above is executed 
for sorting by count as well.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the user tries to sort when not in inventory mode, a CommandException will be thrown by SortCommand to remind user to list first. 

<div>

The following sequence diagram shows how the sort operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

The following activity diagram summarizes what happens when a user executes a Sort command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How Sort executes:**

* **Alternative 1:** Retrieve current inventory and check each item one by one without using the comparator class
    * Pros: Easier to implement
    * Cons: May have performance issue as every command will execute several loops.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* small business owner / entrepreneur
* prefers desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage a small business' inventory and finances faster than a typical mouse/GUI driven app

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                         | I want to …​                                               | So that I can…​                                      |
| -------- | ------------------------------- | ---------------------------------------------------------- | ---------------------------------------------------- |
| `* * *`  | small business owner            | add items into the inventory list                          | account for newly stocked items                      |
| `* * *`  | small business owner            | record item related information (name, price, cost, id)    | record and track items' details easily               |
| `* * *`  | small business owner            | delete items from the inventory                            | account for discarded or sold items                  |
| `* * *`  | small business owner            | look at all my inventory items, with their count           | review and track my inventory                        |
| `* * *`  | small business owner            | record new orders and update the inventory accordingly     | account for newly confirmed orders                   |
| `* * *`  | small business owner            | list past transactions                                     | review verify transaction details                    |
| `* * *`  | budget-savvy business owner     | track the cost that each item incurs                       | manage business costs closely                        |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `Bogo Bogo` and the **Actor** is the `user`, unless specified otherwise)

**UC01 - Adding an item**

**Actor:** User

**MSS**

1. User adds item into inventory.
2. BogoBogo saves item into inventory.

   Use case ends.

**Extensions**

* 1a. User did not specify the name of item.
    * 1a1. BogoBogo notifies user of missing details.

      Use case ends.

* 1b. User is adding the item for the first time, and did not specify the id, price or cost of the item.
    * 1b1. BogoBogo requests user for the missing details.
    * 1b2. User enters the missing details.

      Use case resumes at step 2.

* 1c. The given id does not match with the given name.
    * 1c1. BogoBogo notifies user of the mismatch.

      Use case resumes at step 2.

**UC02 - Deleting an item**

**Actor:** User

**MSS**

1. User requests to delete item from inventory.
2. BogoBogo deletes item from inventory.

   Use case ends.

**Extensions**

* 1a. User did not specify the name or serial number of the item.
    * 1a1. BogoBogo notifies user of missing details.

      Use case ends.

* 1b. The specified item is not in the inventory.
    * 1b1. BogoBogo notifies user that item is not found.

      Use case ends.

* 1c. The given id does not match with the given name.
    * 1c1. BogoBogo notifies user of the mismatch.

      Use case ends.

**UC03 - Finding items through matching keywords**

**Actor:** User

**MSS**

1. User searches for an item in the inventory by stating id, name or tag.
2. BogoBogo finds item from inventory that matches the keywords.

   Use case ends.

**Extensions**

* 1a. User specified an id which is not a positive integer or is not of 6 digits.
    * 1a1. BogoBogo notifies user of invalid id input.

      Use case ends.

* 1b. The specified item is not in the inventory.
    * 1b1. BogoBogo outputs an empty list.

      Use case ends.
  
* 1c. User tries to find by 2 different fields at the same time.
    * 1b1. BogoBogo notifies user that only one field can be inputted.

      Use case ends.

**UC04 - Placing an Order**

**Actor:** User

**MSS**

1. User requests to start an order.
2. BogoBogo creates an order and requests for item names and quantities.
3. User adds an item into the order.
4. BogoBogo saves item into the order.

   Step 3-4 is repeated until the user wishes to end the order.

5. User requests to end entering item and place the order.
6. BogoBogo transacts the order and updates inventory and transaction history.

   Use case ends.

**Extensions**

* 3a. User adds an item that is not in the inventory.
    * 3a1. BogoBogo notifies user that item is not found.

      Use case resumes at step 3.

* 3b. There is an insufficient supply of added items in the inventory.
    * 3a1. BogoBogo notifies user of the supply shortage.

      Use case resumes at step 3.

* 4a. User incorrectly added an item into the order.
    * 3a1. User removes specified item from the order (UC05).

      Use case resumes at step 3.

* 6a. The order is empty.
    * 7a1. BogoBogo notifies user that the order is empty.

      Use case ends.

**UC05 - Remove an item from order**

**Actor:** User

**MSS**

1. User requests to remove the specified item from the order.
2. User enters the item to remove.
3. BogoBogo removes the item from the order.

   Use case ends.

**Extensions**

* 1a. There is no order created.
    * 1a1. BogoBogo notifies user there is no order.
  
      Use case ends.
    
* 2a. The item is specified in wrong format.
    * 2a1. BogoBogo notifies user the item specification format is wrong.
  
      Use case ends.

* 3a. The specified item is not in the order
    * 3a1. BogoBogo notifies user the specified item is not in the order.
  
      Use case ends.

**UC06 - Sorting**

**Actor:** User

**MSS**

1. User requests to sort the inventory (either by name or count).
2. BogoBogo sorts the inventory accordingly.

   Use case ends.
   
**Extensions**

* 1a. User specifies to sort by both name and count.
    * 1a1. BogoBogo notifies user that user can only sort by either name or count, not both.

      Use case ends.

**UC06 - Help**

**Actor:** User

**MSS**

1. User requests to know what are the commands available.
2. BogoBogo shows the commands available to the user.

   Use case ends.
   
 **Extensions**

* 1a. User specifies which command exactly he wants to know how to use.
    * 1a1. BogoBogo notifies the user what that exact command does.

      Use case ends.
  
* 1b. User specifies an inexistent command.
    * 1a1. BogoBogo notifies the user that the command does not exist, then proceed to display URL to userguide.

      Use case ends.

**UC07 - Remove certain amount of item**

**Actor:** User

**MSS**

1. User requests to remove certain amount of an item.
2. User enters the item name and amount.
3. BogoBogo removes the specified amount of that item.

    Use case ends.

**Extensions**

* 2a. The name and amount is specified in wrong format.
    * 2a1. BogoBogo notifies user the format is wrong.
    
        Use case ends.

* 2b. There are multiple matching items in inventory.
    * 2b1. BogoBogo lists out all the matching items and let user choose one.
    * 2b2. User chooses the desired item.
    * BogoBogo removes the specified amount of that item. 

      Use case ends.

* 3a. The item is not in the inventory.
    * 3a1. BogoBogo notifies user there is on such item.

        Use case ends.

* 3b. The specified amount is greater than what inventory has.
    * 3b1. BogoBogo notifies user the actual amount of item in the inventory.

      Use case ends.

**UC08 - Edit an item**

**Actor:** User

**MSS**

1. User requests to edit an item.
2. User enters the item index, fields and new values to change.
3. BogoBogo updates the fields of the item at the specified index with the new values given.

    Use case ends.

**Extensions**

* 3a. The edited item is a duplicate of another item in the inventory.
    * 3a1. BogoBogo notifies user of the duplication.
  
      Use case ends

* 3b. The specified index is invalid.
    * 3b1. BogoBogo notifies user the index is invalid.

      Use case ends
  
* 3c. The specified change of the field is invalid.
    * 3b1. BogoBogo notifies user that the new value specified is invalid.

      Use case ends

**UC09 - Listing out inventory**

**Actor:** User

**MSS**

1. User requests to list out all inventory
2. BogoBogo lists out all items in inventory.

   Use case ends

**UC10 - Listing out order**

**Actor:** User

**MSS**

1. User requests to list out current order.
2. BogoBogo lists out all items in current order.

   Use case ends

**Extensions**

* 2a. There is currently no order to list.
  * 2a1. BogoBogo notifies user there is currently no order.
    
    Use case ends.

**UC11 - Exit the application**

**Actor:** User

**MSS**

1. User requests to exit the application
2. BogoBogo acknowledges the request and exits.

    Use case ends.

**UC12 - Clear the inventory**

**Actor:** User

**MSS**

1. User requests to clear the inventory.
2. BogoBogo acknowledges the request and clears the inventory.
    
    Use case ends.


### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to 1000 distinct inventory items without a noticeable sluggishness in performance for
   typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be
   able to accomplish most of the tasks faster using commands than using the mouse.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Item**: An inventory good that the business owner is/was selling
* **Order**: Information regarding a transaction whereby the business sells a list of items to a customerfor revenue

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

    a. Download the jar file and copy into an empty folder

    b. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be
       optimum.

2. Saving window preferences

    a. Resize the window to an optimum size. Move the window to a different location. Close the window.

    b. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. _{ more test cases …​ }_

### Adding an item

1. Adding a new item into the inventory

    a. Test case: `add Milk id/111111 c/1 sp/2.4 cp/1.2`<br>
       Expected: Item Milk is added to the list. Milk should have the id #111111, count 1, sales price $2.40, and cost price $1.20.

    b. Test case: `add Milk c/1 sp/2.4 sp/1.2`<br>
       Expected: No Milk added to the inventory. BogoBogo notifies user to specify id as well.

    c. Test case: `add n/Milk c/1 sp/2.4 sp/1.2`<br>
       Expected: No Milk added to the inventory. BogoBogo notifies of incorrect command format.

2. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
