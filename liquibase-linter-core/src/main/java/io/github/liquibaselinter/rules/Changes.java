package io.github.liquibaselinter.rules;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import liquibase.change.Change;
import liquibase.change.core.*;

public final class Changes {

    private static final List<Class<? extends Change>> DDL_CHANGE_TYPES = Collections.unmodifiableList(
        Arrays.asList(
            DropViewChange.class,
            AddUniqueConstraintChange.class,
            DropColumnChange.class,
            DropIndexChange.class,
            AddForeignKeyConstraintChange.class,
            ModifyDataTypeChange.class,
            DropNotNullConstraintChange.class,
            RenameTableChange.class,
            MergeColumnChange.class,
            AlterSequenceChange.class,
            CreateIndexChange.class,
            RenameViewChange.class,
            DropPrimaryKeyChange.class,
            DropUniqueConstraintChange.class,
            DropSequenceChange.class,
            RenameSequenceChange.class,
            CreateSequenceChange.class,
            AddNotNullConstraintChange.class,
            DropDefaultValueChange.class,
            AddColumnChange.class,
            DropTableChange.class,
            DropAllForeignKeyConstraintsChange.class,
            CreateViewChange.class,
            CreateTableChange.class,
            RenameColumnChange.class,
            CreateProcedureChange.class,
            DropForeignKeyConstraintChange.class,
            DropProcedureChange.class,
            AddPrimaryKeyChange.class,
            AddDefaultValueChange.class
        )
    );
    private static final List<Class<? extends Change>> DML_CHANGE_TYPES = Collections.unmodifiableList(
        Arrays.asList(
            InsertDataChange.class,
            UpdateDataChange.class,
            DeleteDataChange.class,
            LoadDataChange.class,
            LoadUpdateDataChange.class
        )
    );

    private Changes() {}

    public static boolean isDDL(Change change) {
        return DDL_CHANGE_TYPES.contains(change.getClass());
    }

    public static boolean isDML(Change change) {
        return DML_CHANGE_TYPES.contains(change.getClass());
    }
}
