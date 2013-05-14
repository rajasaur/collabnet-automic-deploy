/**
 * ImportExportServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.uc4.ara.rm;

public class ImportExportServiceLocator extends org.apache.axis.client.Service implements com.uc4.ara.rm.ImportExportService {

/**
 * Data-API
 */

    public ImportExportServiceLocator() {
    }


    public ImportExportServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ImportExportServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ImportExportServiceSoap
    private java.lang.String ImportExportServiceSoap_address = "http://172.16.68.21/Bond2_on_demand-20121016-4-rev19568/service/ImportExportService.asmx";

    public java.lang.String getImportExportServiceSoapAddress() {
        return ImportExportServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ImportExportServiceSoapWSDDServiceName = "ImportExportServiceSoap";

    public java.lang.String getImportExportServiceSoapWSDDServiceName() {
        return ImportExportServiceSoapWSDDServiceName;
    }

    public void setImportExportServiceSoapWSDDServiceName(java.lang.String name) {
        ImportExportServiceSoapWSDDServiceName = name;
    }

    public com.uc4.ara.rm.ImportExportServiceSoap getImportExportServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ImportExportServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getImportExportServiceSoap(endpoint);
    }

    public com.uc4.ara.rm.ImportExportServiceSoap getImportExportServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.uc4.ara.rm.ImportExportServiceSoapStub _stub = new com.uc4.ara.rm.ImportExportServiceSoapStub(portAddress, this);
            _stub.setPortName(getImportExportServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setImportExportServiceSoapEndpointAddress(java.lang.String address) {
        ImportExportServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.uc4.ara.rm.ImportExportServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.uc4.ara.rm.ImportExportServiceSoapStub _stub = new com.uc4.ara.rm.ImportExportServiceSoapStub(new java.net.URL(ImportExportServiceSoap_address), this);
                _stub.setPortName(getImportExportServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ImportExportServiceSoap".equals(inputPortName)) {
            return getImportExportServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://uc4.com/", "ImportExportService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://uc4.com/", "ImportExportServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ImportExportServiceSoap".equals(portName)) {
            setImportExportServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
