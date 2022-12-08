package it.liverif.core.sql.ddl;

import org.hibernate.dialect.MySQL5Dialect;

public class MySQLDialect extends MySQL5Dialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci";
    }
}