# supplier-manual

Manual per-store supplier adapter for CommerceLink. Unlike the other supplier
plugins there is no external feed to download — a store admin uploads a CSV file
by hand and it **overwrites** the previous one. Any number of manual suppliers
can exist at once, each configured per store under
`/dashboard/store/fulfilment`.

Core dependency of the main app (both `dev` and `full` profiles); the app
references its classes directly. **Not** registered via `ServiceLoader`
(`META-INF/services`) — manual suppliers are resolved by the app through
`SupplierProviderFactory` from the `manual:` identity prefix, so they never
trigger external download, scheduling, or secret lookups.

## Identity

A manual supplier is identified by `manual:` + label (e.g.
`manual:Hurtownia Kowalski`). This single string is used consistently as the
store connection name, the S3 feed key, the `InventoryItem` supplier, and the
descriptor name. Label rules: `^[A-Za-z0-9 _-]{1,60}$`, unique per store
(case-insensitive), and must not collide with a registered static supplier.

## Metadata

Canonical `SupplierInfo` is a function of the identity alone — identical for
every manual supplier:

| Attribute | Value |
|-----------|-------|
| Type | Distributor |
| Accuracy score | 5 |
| Origin | PL (local) |
| Shipping | Free |
| Lead time | 2 days (default) |

## CSV format

Imposed, canonical format. Separator `;`, decimal separator **comma**
(`12,50`), first row is a **header** (skipped).

```
ean;mfn;brand;name;category;net_price;currency;qty;lead_time_days
```

Row rules:

| Rule | Behaviour |
|------|-----------|
| Required fields | `(ean` or `mfn)` **and** `name` **and** `net_price`; otherwise the row is skipped |
| `category` | matched case-insensitively to `ProductCategory`, fallback `Other` |
| `qty` | `inStock = qty > 0` |
| `lead_time_days` | defaults to `2` when blank |
| Flags | `sellable = true`, `inDelivery = false` |
| Malformed numbers | the row is skipped (`CsvRowParser.tryParse` swallows the error) |

## Classes

| Class | Responsibility |
|-------|----------------|
| `ManualSupplierInfos` | canonical `SupplierInfo` factory + identity helpers (`PREFIX`, `isManual`, `label`, `identityFor`) |
| `ManualCsvRowParser` | parses one canonical CSV row into `ParsedRow` |
| `ManualSupplierDescriptor` | `SupplierProviderDescriptor` (no download, CSV feed format `;`) |
