package common.BankServerInterfacePackage;

/**
* common/BankServerInterfacePackage/invalid_bankOperationHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from bankserver.idl
* Thursday, November 2, 2017 4:02:20 PM EDT
*/

public final class invalid_bankOperationHolder implements org.omg.CORBA.portable.Streamable
{
  public common.BankServerInterfacePackage.invalid_bankOperation value = null;

  public invalid_bankOperationHolder ()
  {
  }

  public invalid_bankOperationHolder (common.BankServerInterfacePackage.invalid_bankOperation initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = common.BankServerInterfacePackage.invalid_bankOperationHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    common.BankServerInterfacePackage.invalid_bankOperationHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return common.BankServerInterfacePackage.invalid_bankOperationHelper.type ();
  }

}
