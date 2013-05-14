/**
 * ImportExportServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.uc4.ara.rm;

public interface ImportExportServiceSoap extends java.rmi.Remote {
    public com.uc4.ara.rm.Result export(java.lang.String username, java.lang.String password, java.lang.String mainType, java.lang.String customType, int begin, int count, java.lang.String format, java.lang.String[] properties, java.lang.String startDate, java.lang.String endDate, java.lang.String[] conditions) throws java.rmi.RemoteException;
    public com.uc4.ara.rm.Result _import(java.lang.String username, java.lang.String password, java.lang.String mainType, java.lang.String fomat, boolean failOnError, java.lang.String data) throws java.rmi.RemoteException;
    public com.uc4.ara.rm.Result delete(java.lang.String username, java.lang.String password, java.lang.String mainType, java.lang.String fomat, boolean failOnError, java.lang.String data) throws java.rmi.RemoteException;
    public com.uc4.ara.rm.Result getStatus(java.lang.String token) throws java.rmi.RemoteException;
    public com.uc4.ara.rm.Result testCsv(java.lang.String username, java.lang.String password, java.lang.String mainType, java.lang.String[] properties) throws java.rmi.RemoteException;
}
