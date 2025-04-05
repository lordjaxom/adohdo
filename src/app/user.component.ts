import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
    selector: 'app-user',
    templateUrl: './user.component.html',
    styleUrl: './user.component.scss'
})
export class UserComponent {
    @Input() username = '';
    @Output() itemAdded = new EventEmitter();

    addItem() {
        this.itemAdded.emit();
    }
}
