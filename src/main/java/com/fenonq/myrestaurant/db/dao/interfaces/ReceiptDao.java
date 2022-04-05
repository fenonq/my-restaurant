package com.fenonq.myrestaurant.db.dao.interfaces;

import com.fenonq.myrestaurant.db.entity.Receipt;
import com.fenonq.myrestaurant.db.entity.Status;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;

import java.util.List;

public interface ReceiptDao {
    /**
     * Finds all user receipts by user id
     *
     * @param userId id of user
     * @return List of receipts
     */
    List<Receipt> findUserReceipts(int userId, Locales locale) throws DBException;

    /**
     * Finds all receipts in receipt table
     *
     * @return List of receipts
     */
    List<Receipt> findAll(Locales locale) throws DBException;

    /**
     * Finds all receipts accepted by this manager
     *
     * @param managerId id of manager
     * @return List of receipts
     */
    List<Receipt> findAllReceiptsAcceptedBy(int managerId, Locales locale) throws DBException;

    /**
     * Finds all receipts by status id
     *
     * @return List of receipts
     */
    List<Receipt> findByStatusId(int statusId, Locales locale) throws DBException;

    /**
     * Change status of receipt
     *
     * @param receiptId id of receipt
     * @param statusId  new id of status
     * @param managerId id of manager that changed it
     */
    void changeStatus(int receiptId, int statusId, int managerId) throws DBException;

    /**
     * Finds all statuses
     *
     * @return List of statuses
     */
    List<Status> getAllStatus(Locales locale) throws DBException;
}
