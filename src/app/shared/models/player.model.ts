import {Deserializable} from "./deserializable.model";
import {Game} from "./game.model"
import {Link} from "./link.model";
import {forEach} from "@angular/router/src/utils/collection";

export class Player implements Deserializable {
	id: number;
	username: string;
	email: string;
	games: Game[];
	_links: Map<string, Link>;
	
	
	public deserialize(input: any): this {
		Object.assign(this, input);
		
		let list: Game[] = [];
		input.games.forEach(element => {
			list.push(new Game().deserialize(element));
		});
		this.games = list;
		
		if (input.hasOwnProperty('_links')) {
			let list2: Map<string, Link> = new Map<string, Link>();
			Object.keys(input._links).forEach(key => {
				list2.set(key, new Link().deserialize(input._links[key]));
			});
		}
		
		
		return this;
	}
}