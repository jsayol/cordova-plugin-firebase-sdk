import { Reference } from './reference';
import { DataSnapshot } from './data-snapshot';
export declare class Database {
    private static _instance;
    constructor();
    static getInstance(): Database;
    ref(path: string): Reference;
    goOffline(): Promise<void>;
    goOnline(): Promise<void>;
    setPersistenceEnabled(isEnabled: boolean): Promise<void>;
}
export declare type EventListenerCallback = (snapshot: DataSnapshot, prevChildKey?: string) => any;
export interface IDatabase {
    (): Database;
    ServerValue: {
        TIMESTAMP: object;
    };
}
export declare const database: IDatabase;
export { Reference, DataSnapshot };
export { Query } from './query';
export { ThenableReference } from './reference';
export { OnDisconnect } from './on-disconnect';
