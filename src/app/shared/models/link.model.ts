import {Deserializable} from "./deserializable.model";
import {Game} from "./game.model";

export class Link implements Deserializable {
	href: string;
	
	public deserialize(input: any): this {
		Object.assign(this, input);
		return this;
	}
}