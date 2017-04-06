import { Database } from './index';
import { Reference } from './reference';
import { Path } from './utils/path';
export declare class DataSnapshot {
    private _db;
    private _path;
    private _value;
    constructor(_db: Database, _path: Path, _value?: any, key?: string);
    readonly key: string;
    readonly ref: Reference;
    child(path: string): DataSnapshot;
    exists(): boolean;
    hasChild(path: string): boolean;
    hasChildren(): boolean;
    numChildren(): number;
    val(): any;
    forEach(childFn: (childSnap: DataSnapshot) => boolean | void): boolean;
    private _isObjectValue();
}
