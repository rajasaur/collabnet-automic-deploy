package com.uc4.ara.rm;

public class ImportExportServiceSoapProxy implements com.uc4.ara.rm.ImportExportServiceSoap {
  private String _endpoint = null;
  private com.uc4.ara.rm.ImportExportServiceSoap importExportServiceSoap = null;
  
  public ImportExportServiceSoapProxy() {
    _initImportExportServiceSoapProxy();
  }
  
  public ImportExportServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initImportExportServiceSoapProxy();
  }
  
  private void _initImportExportServiceSoapProxy() {
    try {
      importExportServiceSoap = (new com.uc4.ara.rm.ImportExportServiceLocator()).getImportExportServiceSoap();
      if (importExportServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)importExportServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)importExportServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (importExportServiceSoap != null)
      ((javax.xml.rpc.Stub)importExportServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.uc4.ara.rm.ImportExportServiceSoap getImportExportServiceSoap() {
    if (importExportServiceSoap == null)
      _initImportExportServiceSoapProxy();
    return importExportServiceSoap;
  }
  
  public com.uc4.ara.rm.Result export(java.lang.String username, java.lang.String password, java.lang.String mainType, java.lang.String customType, int begin, int count, java.lang.String format, java.lang.String[] properties, java.lang.String startDate, java.lang.String endDate, java.lang.String[] conditions) throws java.rmi.RemoteException{
    if (importExportServiceSoap == null)
      _initImportExportServiceSoapProxy();
    return importExportServiceSoap.export(username, password, mainType, customType, begin, count, format, properties, startDate, endDate, conditions);
  }
  
  public com.uc4.ara.rm.Result _import(java.lang.String username, java.lang.String password, java.lang.String mainType, java.lang.String fomat, boolean failOnError, java.lang.String data) throws java.rmi.RemoteException{
    if (importExportServiceSoap == null)
      _initImportExportServiceSoapProxy();
    return importExportServiceSoap._import(username, password, mainType, fomat, failOnError, data);
  }
  
  public com.uc4.ara.rm.Result delete(java.lang.String username, java.lang.String password, java.lang.String mainType, java.lang.String fomat, boolean failOnError, java.lang.String data) throws java.rmi.RemoteException{
    if (importExportServiceSoap == null)
      _initImportExportServiceSoapProxy();
    return importExportServiceSoap.delete(username, password, mainType, fomat, failOnError, data);
  }
  
  public com.uc4.ara.rm.Result getStatus(java.lang.String token) throws java.rmi.RemoteException{
    if (importExportServiceSoap == null)
      _initImportExportServiceSoapProxy();
    return importExportServiceSoap.getStatus(token);
  }
  
  public com.uc4.ara.rm.Result testCsv(java.lang.String username, java.lang.String password, java.lang.String mainType, java.lang.String[] properties) throws java.rmi.RemoteException{
    if (importExportServiceSoap == null)
      _initImportExportServiceSoapProxy();
    return importExportServiceSoap.testCsv(username, password, mainType, properties);
  }
  
  
}