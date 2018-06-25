import {Deserializable} from "./deserializable.model";
import {Link} from "./link.model";

export class Apipage implements Deserializable {
	size: number;
	totalElements: number;
	totalPages: number;
	number: number;
	
	public deserialize(input: any): this {
		Object.assign(this, input);
		
		return this;
	}
}