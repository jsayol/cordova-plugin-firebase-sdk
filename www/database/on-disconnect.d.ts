import { Database } from './index';
export declare class OnDisconnect {
    private _db;
    private _path;
    constructor(_db: Database, _path: string);
    private _exec(success, error, action, args?);
    set(value: any): Promise<void>;
    remove(): Promise<void>;
    update(value: any): Promise<void>;
    cancel(): Promise<void>;
    setWithPriority(value: any, priority: number | string | null): Promise<void>;
}
