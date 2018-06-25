import {Deserializable} from "./deserializable.model";
import {PlayingField} from "./playingField.model";
import {Link} from "./link.model";

export class Game implements Deserializable {
	id: number;
	playingField?: PlayingField;
	playerOnTurn: number;
	inviteDate: Date;
	startDate?: Date;
	endDate?: Date;
	_links: Map<string, Link>;
	
	public deserialize(input: any): this {
		Object.assign(this, input);
		
		this.playingField = new PlayingField().deserialize(input.playingField);
		
		if (input.hasOwnProperty('_links')) {
			let list2: Map<string, Link> = new Map<string, Link>();
			Object.keys(input._links).forEach(key => {
				list2.set(key, new Link().deserialize(input._links[key]));
			});
		}
		
		return this;
	}
}