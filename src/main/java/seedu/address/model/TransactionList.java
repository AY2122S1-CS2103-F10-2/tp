package seedu.address.model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.order.TransactionRecord;

public class TransactionList implements ReadOnlyTransactionList {

    private ArrayList<TransactionRecord> transactionRecordList;

    public TransactionList(ArrayList<TransactionRecord> transactionRecordList) {
        this.transactionRecordList = transactionRecordList;
    }

    public TransactionList(ReadOnlyTransactionList tobeCopied) {
        this.transactionRecordList = new ArrayList<>(tobeCopied.getTransactionRecordList());
    }

    public TransactionList() {
        this(new ArrayList());
    }

    @Override
    public ObservableList<TransactionRecord> getTransactionRecordList() {
        ObservableList<TransactionRecord> observableList =
                FXCollections.observableList(transactionRecordList);
        return FXCollections.unmodifiableObservableList(observableList);
    }

    /**
     * Adds a {@code TransactionRecord} to the TransactionList.
     */
    public void add(TransactionRecord transactionRecord) {
        this.transactionRecordList.add(transactionRecord);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof TransactionList)) {
            return false;
        }

        // state check
        TransactionList other = (TransactionList) obj;
        return transactionRecordList.equals(other.transactionRecordList);
    }
}
