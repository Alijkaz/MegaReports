package ir.jeykey.megareports.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

@DatabaseTable(tableName = "megareports_meta")
public class ReportMeta {
    @DatabaseField(id = true) @Getter
    @Setter
    private long id;

    @DatabaseField(canBeNull = false) @Getter @Setter
    private long report_id;

    @DatabaseField(canBeNull = false) @Getter @Setter
    private String key;

    @DatabaseField(canBeNull = false) @Getter @Setter
    private String value;

}
