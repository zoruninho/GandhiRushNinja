package GRN.core;

public enum PackType {
	RICE(0),
	MOUSSA(1);
	
	int type;
	
	PackType(int i){
		type=i;
	}
	
	public static PackType getType(int i){
		switch(i){
		case 1:
			return PackType.MOUSSA;
		default:
			return PackType.RICE;
		}
	}
	
	public int getValue(){
		return type;
	}
}
