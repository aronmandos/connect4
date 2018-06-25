import {Deserializable} from "./deserializable.model";
import {Link} from "./link.model";
import {Apipage} from "./apipage.model";
import {Game} from "./game.model";

export class GameApiCollection implements Deserializable {
	games: Game[];
	_links: Map<string, Link>;
	page: Apipage;
	
	
	public deserialize(input: any): this {
		Object.assign(this, input);
		
		let list: Game[] = [];
		input['_embedded']['games'].forEach(element => {
			list.push(new Game().deserialize(element));
		});
		this.games = list;
		
		if (input.hasOwnProperty('_links')) {
			let list2: Map<string, Link> = new Map<string, Link>();
			Object.keys(input._links).forEach(key => {
				list2.set(key, new Link().deserialize(input._links[key]));
			});
		}
		
		this.page = new Apipage().deserialize(input.page);
		
		return this;
	}
}