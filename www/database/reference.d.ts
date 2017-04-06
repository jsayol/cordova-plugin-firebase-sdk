import { Database } from './index';
import { Query } from './query';
import { Path } from './utils/path';
import { OnDisconnect } from './on-disconnect';
export declare class Reference extends Query {
    constructor(path: string | Path, db: Database);
    readonly key: string | null;
    readonly parent: Reference | null;
    readonly root: Reference;
    child(path: string): Reference;
    set(value: any): Promise<void>;
    remove(): Promise<void>;
    push(): Reference;
    push(value: any): ThenableReference;
    setPriority(priority: string | number | null): Promise<void>;
    setWithPriority(value: any, priority: string | number | null): Promise<void>;
    transaction(): void;
    update(value: any): Promise<void>;
    onDisconnect(): OnDisconnect;
}
export declare class ThenableReference extends Reference {
    private _promise;
    set(value: any): Promise<void>;
    then(onfulfilled?: (() => any) | undefined | null, onrejected?: ((reason: any) => any) | undefined | null): Promise<void>;
    catch<T>(onrejected?: ((reason: any) => T) | undefined | null): Promise<T>;
}
