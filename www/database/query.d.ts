import { ErrorCallback, SuccessCallback } from '../utils';
import { Database, EventListenerCallback } from './index';
import { Path } from './utils/path';
import { Reference } from './reference';
export declare class Query {
    protected _path: Path;
    protected _db: Database;
    protected _options: {
        [k: string]: any;
    };
    constructor(query: Query, options: {
        [k: string]: any;
    });
    constructor(path: string | Path, db: Database);
    protected _exec(success: SuccessCallback, error: ErrorCallback, action: string, args: any[]): void;
    readonly ref: Reference;
    startAt(value: number | string | boolean | null, key?: string): Query;
    endAt(value: number | string | boolean | null, key?: string): Query;
    equalTo(value: number | string | boolean | null, key?: string): Query;
    limitToFirst(limit: number): Query;
    limitToLast(limit: number): Query;
    orderByKey(): Query;
    orderByChild(path: string): Query;
    orderByPriority(): Query;
    orderByValue(): Query;
    isEqual(query: Query): boolean;
    on(eventType: string, callback: EventListenerCallback, errorFn?: (err?: Error) => any): EventListenerCallback;
    once(eventType: string, callback?: EventListenerCallback, errorFn?: (err?: Error) => any): Promise<void>;
    private _attachListener(once, eventType, callback, errorFn?);
    off(eventType?: string, callback?: EventListenerCallback): Promise<void>;
    keepSynced(keepSynced: boolean): Promise<void>;
    private _removeListener(id);
    private _addListener(id, path, eventType, callback);
    private _getListenerID(eventType, callback);
    private _getListenerIDs(eventType?, callback?);
}
export interface ListenerEvent {
    key?: string;
    value: any;
    prevChildKey?: string;
}
