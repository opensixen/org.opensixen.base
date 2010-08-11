package org.opensixen.model;

public class QueryParam {
		private String columnName;
		private Object value;
		private Class valueClass;
		private int columnIndex;
		private int columnDataType;
		
		public QueryParam(String columnName, Object value, Class c, int columnIndex, int columnDataType) {
			this.columnName = columnName;
			this.value = value;
			this.valueClass = c;
			this.columnIndex = columnIndex;
			this.columnDataType = columnDataType;
		}
		
		public String getColumnName() {
			return columnName;
		}
		
		public Object getValue() {
			return value;
		}
		
		public Class getValueClass() {
			return valueClass;
		}
		
		public int getColumnIndex() {
			return columnIndex;
		}
		
		public int getColumnDataType() {
			return columnDataType;
		}
	
	
}
