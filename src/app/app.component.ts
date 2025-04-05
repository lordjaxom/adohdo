import {Component} from '@angular/core';
import {UserComponent} from "./user.component";

@Component({
    selector: 'app-root',
    imports: [UserComponent],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent {
    title = 'Client';
    searchUrl = 'https://www.google.com/search?q=';
    username = 'Sascha Volkenandt';
    items = 0;

    onButtonClick() {
      this.title = 'Clicked';
    }

    addItem() {
        ++this.items;
    }
}
