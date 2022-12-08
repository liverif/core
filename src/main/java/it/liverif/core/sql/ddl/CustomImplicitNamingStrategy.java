package it.liverif.core.sql.ddl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.boot.model.naming.*;
import java.util.stream.Collectors;

public class CustomImplicitNamingStrategy extends ImplicitNamingStrategyLegacyHbmImpl {
    private static final Logger logger = LogManager.getLogger(CustomImplicitNamingStrategy.class);

    @Override
    public Identifier determineIndexName(ImplicitIndexNameSource source) {
        String name = "IX_" + source.getTableName().getCanonicalName() + "_" + source.getColumnNames()
                .stream()
                .map(x -> x.toString())
                .collect(Collectors.joining("_"));
        if (name.length() > 64) {
            name = name.substring(0, 59) + DigestUtils.md5Hex(name).toUpperCase().substring(0, 4);
        }
        return toIdentifier(name, source.getBuildingContext());
    }

    @Override
    public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
        String name = "UK_" + source.getTableName().getCanonicalName() + "_" + source.getColumnNames()
                .stream()
                .map(x -> x.toString())
                .collect(Collectors.joining("_"));
        if (name.length() > 64) {
            name = name.substring(0, 59) + DigestUtils.md5Hex(name).toUpperCase().substring(0, 4);
        }
        return toIdentifier(name, source.getBuildingContext());
    }

    @Override
    public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
        String name = "FK_" + source.getTableName() + "_" + source.getColumnNames()
                .stream()
                .map(x -> x.toString())
                .collect(Collectors.joining("_"));
        if (name.length() > 64) {
            name = name.substring(0, 59) + DigestUtils.md5Hex(name).toUpperCase().substring(0, 4);
        }
        return toIdentifier(name, source.getBuildingContext());
    }

}