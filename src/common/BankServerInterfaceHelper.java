package common;


/**
* common/BankServerInterfaceHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from bankserver.idl
* Tuesday, October 31, 2017 11:43:13 PM EDT
*/


//Interface for the Banking Server
abstract public class BankServerInterfaceHelper
{
  private static String  _id = "IDL:common/BankServerInterface:1.0";

  public static void insert (org.omg.CORBA.Any a, common.BankServerInterface that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static common.BankServerInterface extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (common.BankServerInterfaceHelper.id (), "BankServerInterface");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static common.BankServerInterface read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_BankServerInterfaceStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, common.BankServerInterface value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static common.BankServerInterface narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof common.BankServerInterface)
      return (common.BankServerInterface)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      common._BankServerInterfaceStub stub = new common._BankServerInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static common.BankServerInterface unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof common.BankServerInterface)
      return (common.BankServerInterface)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      common._BankServerInterfaceStub stub = new common._BankServerInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}