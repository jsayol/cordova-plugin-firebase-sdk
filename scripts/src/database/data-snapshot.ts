import { Database } from './index';
import { Reference } from './reference';
import { Path } from './utils/path';

export class DataSnapshot {
  constructor(private _db: Database, private _path: Path, private _value: any = null, key?: string) {
    if (typeof key === 'string') {
      this._path = this._path.child(key);
    }
  }

  get key(): string {
    return this._path.key;
  }

  get ref(): Reference {
    return new Reference(this._path, this._db);
  }

  child(path: string) {
    let pathParts = Path.getParts(path);
    let value = this._value;

    while (pathParts.length) {
      const childKey = pathParts.shift();
      if ((typeof value === 'undefined') || (value === null) || !value.hasOwnProperty(childKey)) {
        value = null;
        break;
      }

      value = value[childKey];
    }

    return new DataSnapshot(this._db, this._path.child(path), value);
  }

  exists(): boolean {
    return (typeof this._value !== 'undefined') && (this._value !== null);
  }

  hasChild(path: string): boolean {
    let pathParts = Path.getParts(path);
    let value = this._value;

    while (pathParts.length) {
      const childKey = pathParts.shift();
      if ((typeof value === 'undefined') || (value === null) || !value.hasOwnProperty(childKey)) {
        return false;
      }

      value = value[childKey];
    }

    return ((typeof value !== 'undefined') && (value !== null));
  }

  hasChildren(): boolean {
    if (!this._isObjectValue()) {
      return false;
    }

    return Object.keys(this._value)
      .some((key: string) => (typeof this._value[key] !== 'undefined') && (this._value[key] !== null));
  }

  numChildren(): number {
    if (!this._isObjectValue()) {
      return 0;
    }

    return Object.keys(this._value)
      .find((key: string) => (typeof this._value[key] !== 'undefined') && (this._value[key] !== null))
      .length;
  }

  val(): any {
    return this._value;
  }

  forEach(childFn: (childSnap: DataSnapshot) => boolean | void): boolean {
    // TODO: loop the child keys in the same order that was specified in the query that generated this snapshot

    if (!this._isObjectValue()) {
      return false;
    }

    const keys = Object.keys(this._value);
    for (let i = 0, l = keys.length; i < l; i++) {
      if (childFn(this.child(keys[i]))) {
        return true;
      }
    }

    return false;
  }

  private _isObjectValue(): boolean {
    if ((typeof this._value === 'undefined') || (this._value === null) || Array.isArray(this._value)) {
      return false;
    }

    return (typeof this._value === 'object');
  }

}
