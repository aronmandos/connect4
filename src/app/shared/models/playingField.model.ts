import {Deserializable} from "./deserializable.model";

export class PlayingField implements Deserializable {
	places: any[][];
	height: number;
	width: number;
	
	
	public deserialize(input: any): this {
		Object.assign(this, input);
		return this;
	}
}