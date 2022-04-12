package it.liverif.core.repository;

public interface IFileDbModelBean {

    public String getFilename();
    public void setFilename(String filename);

    public String getContenttype();
    public void setContenttype(String contenttype);

    public byte[] getData();
    public void setData(byte[] data);

}
