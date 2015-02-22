/*
 * Copyright 2014 Kevin Quan (kevin.quan@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevinquan.android.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.kevinquan.android.utils.DeviceUtils;
import com.kevinquan.android.utils.DeviceUtils.Permissions;

/**
 * Utility methods to access accounts on the device
 *
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class AccountUtils {

    /**
     * Checks whether the account's name is an email address
     * @param account The account to check
     * @return True if the account name is an email address.
     */
    public static boolean doesAccountHaveEmail(Account account) {
        if (account == null || TextUtils.isEmpty(account.name)) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(account.name).matches();
    }

    /**
     * Retrieves all accounts on the phone.
     * @param context  The context to use
     * @return A list of all accounts (may be empty)
     */
    public static List<Account> getAllAccounts(Context context) {
        List<Account> accounts = new ArrayList<Account>();
        if (context == null) {
            return accounts;
        }
        if (!DeviceUtils.hasPermission(context, Permissions.GET_ACCOUNTS)) {
        	Log.w(TAG, "App does not have permission to retrieve accounts");
        	return accounts;
        }
        Account[] allAccounts = AccountManager.get(context).getAccounts();
        if (allAccounts == null) {
            return accounts;
        }
        return Arrays.asList(allAccounts);
    }

    /**
     * Retrieves all accounts corresponding to a specific type
     * @param context  The context to use
     * @param accountType The type of account to retrieve
     * @return A list of accounts that match the provided type
     */
    public static List<Account> getAllAccountsOf(Context context, String accountType) {
        List<Account> accounts = new ArrayList<Account>();
        if (context == null || TextUtils.isEmpty(accountType)) {
            return accounts;
        }
        if (!DeviceUtils.hasPermission(context, Permissions.GET_ACCOUNTS)) {
        	Log.w(TAG, "App does not have permission to retrieve accounts");
        	return accounts;
        }
        Account[] allAccounts = AccountManager.get(context).getAccounts();
        if (allAccounts == null) {
            return accounts;
        }
        for (Account account : allAccounts) {
            if (accountType.equals(account.type)) {
                accounts.add(account);
            }
        }
        return accounts;
    }

    /**
     * Returns the first account that matches the provided account type
     * @param context  The context to use
     * @param accountType The account type to search for
     * @return The first account if one exists
     */
    public static Account getFirstAccountsOf(Context context, String accountType) {
        if (context == null || TextUtils.isEmpty(accountType)) {
            return null;
        }
        if (!DeviceUtils.hasPermission(context, Permissions.GET_ACCOUNTS)) {
        	Log.w(TAG, "App does not have permission to retrieve accounts");
        	return null;
        }
        Account[] allAccounts = AccountManager.get(context).getAccounts();
        if (allAccounts == null) {
            return null;
        }
        for (Account account : allAccounts) {
            if (accountType.equals(account.type)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Checks whether there is an account that matches the provided account
     * @param context  The context to use
     * @param currentAccount The account to match
     * @param accountType The expected account type
     * @return True if the account exists on the device
     */
    public static boolean hasAccount(Context context, Account currentAccount, String accountType) {
        if (context == null || TextUtils.isEmpty(accountType)) {
            return false;
        }
        if (!DeviceUtils.hasPermission(context, Permissions.GET_ACCOUNTS)) {
        	Log.w(TAG, "App does not have permission to retrieve accounts");
        	return false;
        }
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(accountType);
        if (accounts == null) {
            return false;
        }
        for (Account account : accounts) {
            if (currentAccount.equals(account)) {
                return true;
            }
        }
        return false;
    }

    private static final String TAG = AccountUtils.class.getSimpleName();

    public static final String ACCOUNT_TYPE_GOOGLE = "com.google";

}
