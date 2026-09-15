# Catalog Workbook Analysis

This document captures the analysis phase for the AI Enablement demo portal. The workbook supplied by the user is treated as a source reference only. The implementation should use a sanitized demo workbook and must not copy customer-specific names, ticket identifiers, operational references, or branding into generated project data.

## 1. Workbook overview

- Source: workbook supplied in the project root by the user.
- Size: 28.77 MB.
- Sheets: 16.
- Hidden sheets: none observed; all sheets are visible.
- Header pattern: most catalog sheets use row 1 as grouped metadata/section labels and row 2 as the actual column header row.
- Main data volume: `Price List Items` is the largest sheet with 174,852 non-empty data rows.
- Runtime recommendation: do not read this Excel file on every API request. Generate normalized JSON at build/import time and serve that JSON from Quarkus.

## 2. Sheet list

| Sheet | State | Rows | Columns | Header row | Merged ranges | Purpose |
|---|---:|---:|---:|---:|---:|---|
| `Cover Page` | visible | 26 | 3 | 24 | 0 | Cover/status page. Not a normalized catalog table; useful only as source documentation. |
| `Revision History` | visible | 641 | 6 | 1 | 0 | Change log with versions, dates, authors/reasons and internal ticket references. Useful for audit only, not for demo runtime catalog. |
| `Offerings` | visible | 727 | 186 | 2 | 20 | Master configuration for categories, templates, relations, characteristics and price-component metadata. Too wide for direct portal display; useful for taxonomy and parameter definitions. |
| `Equipment Offerings` | visible | 2281 | 31 | 2 | 2 | Primary catalog product sheet for serialized equipment items. Best MVP product source. |
| `Non-serialized Equipment` | visible | 5 | 29 | 2 | 2 | Small product sheet for non-serialized accessories. Same shape as equipment; include in MVP as product source. |
| `Other Offerings` | visible | 19 | 29 | 2 | 2 | Technical/service offerings around equipment sales, delivery, refund, reversal and AppleCare-like services. Include selectively or mark as technical. |
| `Price List Items` | visible | 174854 | 38 | 2 | 0 | Large price fact table joined to products by offering name/template. Best source for catalog price, currency, sale type and pricing options. |
| `Relations Overrides` | visible | 3827 | 17 | 2 | 0 | Parent/child relationship overrides. Best source for category-to-product and product-to-product dependencies/exclusions. |
| `Characteristics Overrides` | visible | 29 | 10 | 2 | 0 | Per-offering characteristic visibility/modifiability overrides. Useful as product parameter metadata. |
| `Available Values` | visible | 1158 | 7 | 2 | 0 | Allowed values for configurable characteristics such as sale type and commitment period. Useful for enum extraction. |
| `Flat Discounts` | visible | 26 | 70 | 2 | 2 | Discount definitions and discounted offering/category targeting. Useful for optional promo/discount flags, not core MVP. |
| `Flat Rules` | visible | 82 | 75 | 2 | 5 | Rule definitions for conflicts, discounts and characteristic dependencies. Useful for dependency/condition examples. |
| `Complex Flat Rules` | visible | 7 | 70 | 2 | 4 | Small set of multi-condition dependency rules. Good source for demo edge cases. |
| `Characteristic State Rules` | visible | 14 | 21 | 2 | 2 | Rules that change characteristic visibility/mandatory/modifiable state. Useful for advanced training scenarios. |
| `Prices` | visible | 4 | 61 | 2 | 8 | Template-level price component configuration. Secondary source; not needed for MVP product list. |
| `Price Variables` | visible | 8 | 7 | 2 | 0 | Declared price-variable columns but no actual data rows. Treat as placeholder/service sheet. |

## 3. Column tables by sheet

The `Required guess` column is inferred from actual fill rate, not from a formal schema. `required-like` means the column is filled in at least 99% of data rows.

### Cover Page

Purpose: Cover/status page. Not a normalized catalog table; useful only as source documentation. Data rows observed: 2.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 2 | `Status` | 100.0% | string:2 | required-like |
| 3 | `Draft` | 100.0% | date:1, string:1 | required-like |

### Revision History

Purpose: Change log with versions, dates, authors/reasons and internal ticket references. Useful for audit only, not for demo runtime catalog. Data rows observed: 635.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Version` | 99.2% | string:635 | required-like |
| 2 | `Date` | 99.2% | date:635 | required-like |
| 3 | `Author` | 99.2% | string:635 | required-like |
| 4 | `Reason of change` | 99.2% | string:635 | required-like |
| 5 | `JSD Ticket` | 33.1% | string:212 | optional/sparse |
| 6 | `Description` | 99.2% | string:635 | required-like |

### Offerings

Purpose: Master configuration for categories, templates, relations, characteristics and price-component metadata. Too wide for direct portal display; useful for taxonomy and parameter definitions. Data rows observed: 725.
Merged/group headers: 20 ranges, sample `AZ1:BA1, P1:Q1, EL1:EN1, CD1:CE1, EO1:EV1, FC1:FM1`.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `ID For Migration Stream` | 100.0% | integer:725 | required-like |
| 2 | `Iteration` | 100.0% | string:725 | required-like |
| 3 | `Design Task` | 100.0% | string:725 | required-like |
| 4 | `JSD Ticket` | 22.8% | string:165 | optional/sparse |
| 5 | `Type` | 100.0% | string:725 | required-like |
| 6 | `Template` | 0.0% | blank | empty |
| 7 | `Category` | 13.5% | string:98 | optional/sparse |
| 8 | `Offering` | 86.5% | string:627 | mostly filled |
| 9 | `Next Level Entity` | 91.6% | string:664 | mostly filled |
| 10 | `Next Level Entity Type` | 91.6% | string:664 | mostly filled |
| 11 | `Order Number` | 95.3% | integer:691 | mostly filled |
| 12 | `Child Entities Count Min` | 23.2% | integer:168 | optional/sparse |
| 13 | `Child Entities Count Max` | 23.2% | integer:168 | optional/sparse |
| 14 | `Default Behavior` | 3.6% | string:26 | optional/sparse |
| 15 | `Offerings Include Action` | 2.5% | string:18 | optional/sparse |
| 16 | `Show in Bulk Tree` | 0.0% | blank | empty |
| 17 | `Hide In Details Page` | 1.7% | string:12 | optional/sparse |
| 18 | `Show When Parent BPI Status In` | 0.0% | blank | empty |
| 19 | `Widget` | 0.1% | string:1 | optional/sparse |
| 20 | `Is Visible` | 4.0% | string:29 | optional/sparse |
| 21 | `External Id` | 0.0% | blank | empty |
| 22 | `Relationships` | 0.0% | blank | empty |
| 23 | `Category Name (Arabic)` | 1.9% | string:14 | optional/sparse |
| 24 | `OM Mapping` | 26.3% | string:191 | optional/sparse |
| 25 | `OM Product Spec` | 4.1% | string:30 | optional/sparse |
| 26 | `OM Product Component` | 0.1% | string:1 | optional/sparse |
| 27 | `Map to OM Attribute` | 0.0% | blank | empty |
| 28 | `OM` | 0.0% | blank | empty |
| 29 | `Offering Object Type` | 4.4% | string:32 | optional/sparse |
| 30 | `Canary Candidate` | 0.0% | blank | empty |
| 31 | `Is Top Offer?` | 4.4% | string:32 | optional/sparse |
| 32 | `Is Voucher Action Offering` | 0.0% | blank | empty |
| 33 | `Custom Logic` | 0.0% | blank | empty |
| 34 | `Description` | 13.9% | string:101 | optional/sparse |
| 35 | `Suspendable` | 3.5% | string:25 | optional/sparse |
| 36 | `Tangible` | 0.0% | blank | empty |
| 37 | `Available for Users` | 0.0% | blank | empty |
| 38 | `Not Available for Users` | 0.0% | blank | empty |
| 39 | `Can be changed to` | 0.0% | blank | empty |
| 40 | `Transfer To` | 0.0% | blank | empty |
| 41 | `Serviceability Access Type` | 0.0% | blank | empty |
| 42 | `Requires Contract` | 2.3% | string:17 | optional/sparse |
| 43 | `Contract Form` | 0.0% | blank | empty |
| 44 | `Business Domain` | 0.0% | blank | empty |
| 45 | `Available Only For Campaigns` | 0.0% | blank | empty |
| 46 | `Eligible for B2B Type B` | 0.1% | string:1 | optional/sparse |
| 47 | `Not Allowed For Black-listed Customers` | 2.2% | string:16 | optional/sparse |
| 48 | `Document Types` | 0.0% | blank | empty |
| 49 | `Offering Group` | 0.0% | blank | empty |
| 50 | `Partial Revert Changes` | 0.0% | blank | empty |
| 51 | `Prepaid Wallet Offering` | 0.0% | blank | empty |
| 52 | `Reservation Necessity` | 2.1% | string:15 | optional/sparse |
| 53 | `Can Be Returned` | 2.1% | string:15 | optional/sparse |
| 54 | `OTS Template` | 2.1% | string:15 | optional/sparse |
| 55 | `Prepayment Required For` | 2.6% | string:19 | optional/sparse |
| 56 | `Change Ownership Validation` | 0.0% | blank | empty |
| 57 | `Relocate Validation` | 0.0% | blank | empty |
| 58 | `Transfer Validation` | 0.0% | blank | empty |
| 59 | `Offering and Equipment` | 0.0% | blank | empty |
| 60 | `Characteristic Name` | 72.1% | string:523 | optional/sparse |
| 61 | `External Characteristic` | 0.0% | blank | empty |
| 62 | `Characteristic Type` | 72.1% | string:523 | optional/sparse |
| 63 | `Is Multiple?` | 72.1% | string:523 | optional/sparse |
| 64 | `Selected Values Min` | 4.1% | integer:30 | optional/sparse |
| 65 | `Selected Values Max` | 4.1% | integer:30 | optional/sparse |
| 66 | `Attribute Name` | 72.1% | string:523 | optional/sparse |
| 67 | `Item Model Attribute` | 7.2% | string:52 | optional/sparse |
| 68 | `Data Provider Name` | 3.9% | string:28 | optional/sparse |
| 69 | `Resource Provider Name` | 0.0% | blank | empty |
| 70 | `Mask` | 0.7% | string:5 | optional/sparse |
| 71 | `Min` | 0.0% | blank | empty |
| 72 | `Max` | 0.0% | blank | empty |
| 73 | `Exported in` | 0.0% | blank | empty |
| 74 | `Business Type` | 0.0% | blank | empty |
| 75 | `Attribute Scope` | 72.1% | string:523 | optional/sparse |
| 76 | `Custom Value Update Provider Name` | 0.0% | blank | empty |
| 77 | `Custom Value Update Events` | 0.0% | blank | empty |
| 78 | `Show in Portal` | 0.0% | blank | empty |
| 79 | `Values Order Number` | 0.0% | blank | empty |
| 80 | `Characteristic Values Name` | 23.7% | string:171, integer:1 | optional/sparse |
| 81 | `Characteristic Values` | 23.7% | string:171, integer:1 | optional/sparse |
| 82 | `Characteristic Values Name (Arabic)` | 0.0% | blank | empty |
| 83 | `Characteristic Involvement Name (Arabic)` | 29.4% | string:213 | optional/sparse |
| 84 | `Associated with OPF Char Values` | 0.0% | blank | empty |
| 85 | `Phone Ranges` | 0.0% | blank | empty |
| 86 | `Characteristic and Values` | 0.0% | blank | empty |
| 87 | `Characteristic Number` | 15.7% | integer:114 | optional/sparse |
| 88 | `Available Values` | 21.7% | string:155, integer:2 | optional/sparse |
| 89 | `Default Value` | 6.9% | string:38, integer:12 | optional/sparse |
| 90 | `Excluded Values` | 0.0% | blank | empty |
| 91 | `Visible in CSRD` | 0.5% | boolean:4 | optional/sparse |
| 92 | `Visible in OE/CPQ` | 72.1% | boolean:452, string:71 | optional/sparse |
| 93 | `Mandatory` | 72.1% | boolean:452, string:71 | optional/sparse |
| 94 | `Modifiable` | 72.1% | boolean:454, string:69 | optional/sparse |
| 95 | `Common Characteristic Involvement` | 0.0% | blank | empty |
| 96 | `Unique Characteristic Involvement` | 6.2% | string:45 | optional/sparse |
| 97 | `Values Price Dependent` | 0.0% | blank | empty |
| 98 | `Propagate to` | 0.0% | blank | empty |
| 99 | `Propagate Entered Value to` | 0.0% | blank | empty |
| 100 | `Modifiable if propagated` | 0.0% | blank | empty |
| 101 | `Scope of Context Variable` | 1.7% | string:12 | optional/sparse |
| 102 | `Exclude from Propagation Scope` | 0.0% | blank | empty |
| 103 | `Is Alias` | 0.0% | blank | empty |
| 104 | `Bucket Allowance Multiplier` | 0.0% | blank | empty |
| 105 | `Inv. Independent` | 0.0% | blank | empty |
| 106 | `Freeze MSA` | 0.0% | blank | empty |
| 107 | `Inv. Resource Provider Name` | 0.0% | blank | empty |
| 108 | `Characteristic Involvement & Default Value` | 0.0% | blank | empty |
| 109 | `Down Payment Type` | 0.0% | blank | empty |
| 110 | `Installment Period` | 0.0% | blank | empty |
| 111 | `Installment Period Unit` | 0.0% | blank | empty |
| 112 | `Down Payment Revenue Code` | 0.0% | blank | empty |
| 113 | `Price Key Name of Installment Technical OTS` | 0.0% | blank | empty |
| 114 | `Installment Technical OTS` | 0.0% | blank | empty |
| 115 | `Price Key Name of Refund Installment Technical OTS` | 0.0% | blank | empty |
| 116 | `OTS Price Node` | 0.0% | blank | empty |
| 117 | `OTS Price` | 0.0% | blank | empty |
| 118 | `Charge Reversal Adjustment Type` | 0.0% | blank | empty |
| 119 | `Refund Reverse Adjustment Type` | 0.0% | blank | empty |
| 120 | `Installment Taxation` | 0.0% | blank | empty |
| 121 | `Down Payment` | 0.0% | blank | empty |
| 122 | `Down Payment Min` | 0.0% | blank | empty |
| 123 | `Down Payment Max` | 0.0% | blank | empty |
| 124 | `Down Payment in Percent` | 0.0% | blank | empty |
| 125 | `Down Payment in Percent Min` | 0.0% | blank | empty |
| 126 | `Down Payment in Percent Max` | 0.0% | blank | empty |
| 127 | `Installment Plan details` | 0.0% | blank | empty |
| 128 | `Installment Plans` | 0.0% | blank | empty |
| 129 | `Export to The External System` | 4.4% | string:32 | optional/sparse |
| 130 | `Billing Specification` | 0.1% | string:1 | optional/sparse |
| 131 | `Invoicing Company` | 4.4% | string:32 | optional/sparse |
| 132 | `Billing Method` | 0.0% | blank | empty |
| 133 | `Community Group Type` | 0.0% | blank | empty |
| 134 | `Prepayment Decision` | 0.0% | blank | empty |
| 135 | `One-Time Service Type` | 2.1% | string:15 | optional/sparse |
| 136 | `OTS Tariff Event Type` | 1.7% | integer:12 | optional/sparse |
| 137 | `Realtime Charge` | 2.1% | string:15 | optional/sparse |
| 138 | `Allow installment Billing` | 2.1% | string:15 | optional/sparse |
| 139 | `Refund One-Time Service` | 0.3% | string:2 | optional/sparse |
| 140 | `Trigger Event Specification` | 0.0% | blank | empty |
| 141 | `Price Key Name of Refund Technical OTS` | 0.3% | string:2 | optional/sparse |
| 142 | `Renewable Event Type` | 0.0% | blank | empty |
| 143 | `Renewable One-Time Service Type` | 0.0% | blank | empty |
| 144 | `Upfront One-Time Service Type` | 0.0% | blank | empty |
| 145 | `Bill Attribute Name` | 4.1% | string:30 | optional/sparse |
| 146 | `Attribute Unit` | 4.1% | string:30 | optional/sparse |
| 147 | `Attribute Type` | 4.1% | string:30 | optional/sparse |
| 148 | `Product Attribute Sub ID` | 4.1% | integer:30 | optional/sparse |
| 149 | `Display Position` | 3.9% | integer:28 | optional/sparse |
| 150 | `Attribute Order Number` | 4.1% | integer:30 | optional/sparse |
| 151 | `Override Billing Mandatory` | 0.5% | boolean:2, string:2 | optional/sparse |
| 152 | `Predicate Attribute` | 0.0% | blank | empty |
| 153 | `NRM Parameters` | 0.0% | blank | empty |
| 154 | `Start Date` | 0.3% | string:2 | optional/sparse |
| 155 | `End Date` | 0.0% | blank | empty |
| 156 | `Is Default` | 0.1% | string:1 | optional/sparse |
| 157 | `Point of Supply` | 0.1% | string:1 | optional/sparse |
| 158 | `Tax Set` | 0.1% | string:1 | optional/sparse |
| 159 | `Tax Inclusive` | 0.1% | string:1 | optional/sparse |
| 160 | `Track Price Changes` | 0.1% | string:1 | optional/sparse |
| 161 | `Price Plan Type` | 0.1% | string:1 | optional/sparse |
| 162 | `In Advance` | 0.1% | string:1 | optional/sparse |
| 163 | `Prorate` | 0.1% | string:1 | optional/sparse |
| 164 | `Refundable` | 0.1% | string:1 | optional/sparse |
| 165 | `Status Change Chargings. On Activation` | 0.1% | string:1 | optional/sparse |
| 166 | `Status Change Chargings. On Reactivation` | 0.1% | string:1 | optional/sparse |
| 167 | `Status Change Chargings. On Suspension` | 0.1% | string:1 | optional/sparse |
| 168 | `Status Change Chargings. On Termination` | 0.1% | string:1 | optional/sparse |
| 169 | `Merge Tariff` | 0.0% | blank | empty |
| 170 | `Charge Period` | 0.1% | integer:1 | optional/sparse |
| 171 | `Generate No Charge` | 0.1% | string:1 | optional/sparse |
| 172 | `Tax Involvement / Offering Price Details` | 0.0% | blank | empty |
| 173 | `Event Source Template Name` | 0.0% | blank | empty |
| 174 | `Event Specification` | 0.0% | blank | empty |
| 175 | `Event Source Mask` | 0.0% | blank | empty |
| 176 | `Label on Bill Mask` | 0.0% | blank | empty |
| 177 | `Event Source Template` | 0.0% | blank | empty |
| 178 | `Upfront OTC ID` | 0.0% | blank | empty |
| 179 | `Upfront Pseudo Event Type ID` | 0.0% | blank | empty |
| 180 | `OTC ID` | 1.9% | integer:14 | optional/sparse |
| 181 | `Pseudo Event Type ID` | 1.9% | integer:14 | optional/sparse |
| 182 | `OTC Installment Absolute ID` | 0.3% | integer:2 | optional/sparse |
| 183 | `Pseudo Event Type Installment Absolute ID` | 0.3% | integer:2 | optional/sparse |
| 184 | `OTC Installment Percent ID` | 0.3% | integer:2 | optional/sparse |
| 185 | `Pseudo Event Type Installment Percent ID` | 0.3% | integer:2 | optional/sparse |
| 186 | `Event Filter ID` | 0.3% | integer:2 | optional/sparse |

### Equipment Offerings

Purpose: Primary catalog product sheet for serialized equipment items. Best MVP product source. Data rows observed: 2279.
Merged/group headers: 2 ranges, sample `Z1:AE1, P1:Y1`.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:2279 | required-like |
| 2 | `Design task` | 100.0% | string:2279 | required-like |
| 3 | `JSD Ticket` | 63.5% | string:1447 | optional/sparse |
| 4 | `Offering Id` | 100.0% | integer:2279 | required-like |
| 5 | `External Id` | 100.0% | integer:2279 | required-like |
| 6 | `Technical Categories` | 31.3% | string:714 | optional/sparse |
| 7 | `Categories` | 0.1% | string:3 | optional/sparse |
| 8 | `Offering Template` | 100.0% | string:2279 | required-like |
| 9 | `Offering Name` | 100.0% | string:2279 | required-like |
| 10 | `Display Name` | 100.0% | string:2279 | required-like |
| 11 | `Billing Name` | 100.0% | string:2279 | required-like |
| 12 | `Offering Group` | 0.0% | blank | empty |
| 13 | `Order Number` | 100.0% | integer:2279 | required-like |
| 14 | `Weight` | 0.0% | blank | empty |
| 15 | `Tags` | 98.9% | string:2254 | mostly filled |
| 16 | `Available From` | 100.0% | string:2279 | required-like |
| 17 | `Available To` | 24.8% | string:564 | optional/sparse |
| 18 | `Eliminated From` | 0.0% | blank | empty |
| 19 | `Archived From` | 0.0% | blank | empty |
| 20 | `Flat Full Eligibility Condition` | 100.0% | string:2279 | required-like |
| 21 | `Is Bundle Only` | 0.0% | blank | empty |
| 22 | `Description` | 0.1% | string:2 | optional/sparse |
| 23 | `Lifecycle Offering Details` | 0.0% | blank | empty |
| 24 | `Product Family` | 98.7% | string:2249 | mostly filled |
| 25 | `Localization Needed` | 100.0% | string:2279 | required-like |
| 26 | `SKU ID` | 98.7% | string:2249 | mostly filled |
| 27 | `AppleCare Model` | 19.5% | string:445 | optional/sparse |
| 28 | `Delivery Type` | 0.0% | blank | empty |
| 29 | `Subsidy Amount` | 1.1% | integer:25 | optional/sparse |
| 30 | `Equipment Subsidy Commitment Period` | 0.8% | integer:17 | optional/sparse |
| 31 | `Partner Offer Total Amount` | 0.2% | integer:5 | optional/sparse |

### Non-serialized Equipment

Purpose: Small product sheet for non-serialized accessories. Same shape as equipment; include in MVP as product source. Data rows observed: 3.
Merged/group headers: 2 ranges, sample `Z1:AC1, P1:Y1`.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:3 | required-like |
| 2 | `Design task` | 100.0% | string:3 | required-like |
| 3 | `JSD Ticket` | 33.3% | string:1 | optional/sparse |
| 4 | `Offering Id` | 100.0% | integer:3 | required-like |
| 5 | `External Id` | 100.0% | integer:3 | required-like |
| 6 | `Technical Categories` | 0.0% | blank | empty |
| 7 | `Categories` | 0.0% | blank | empty |
| 8 | `Offering Template` | 100.0% | string:3 | required-like |
| 9 | `Offering Name` | 100.0% | string:3 | required-like |
| 10 | `Display Name` | 100.0% | string:3 | required-like |
| 11 | `Billing Name` | 100.0% | string:3 | required-like |
| 12 | `Offering Group` | 0.0% | blank | empty |
| 13 | `Order Number` | 100.0% | integer:3 | required-like |
| 14 | `Weight` | 0.0% | blank | empty |
| 15 | `Tags` | 100.0% | string:3 | required-like |
| 16 | `Available From` | 100.0% | string:3 | required-like |
| 17 | `Available To` | 33.3% | string:1 | optional/sparse |
| 18 | `Eliminated From` | 0.0% | blank | empty |
| 19 | `Archived From` | 0.0% | blank | empty |
| 20 | `Flat Full Eligibility Condition` | 100.0% | string:3 | required-like |
| 21 | `Is Bundle Only` | 0.0% | blank | empty |
| 22 | `Description` | 0.0% | blank | empty |
| 23 | `Lifecycle Offering Details` | 0.0% | blank | empty |
| 24 | `Product Family` | 100.0% | string:3 | required-like |
| 25 | `Localization Needed` | 100.0% | string:3 | required-like |
| 26 | `SKU ID` | 100.0% | string:3 | required-like |
| 27 | `Delivery Type` | 0.0% | blank | empty |
| 28 | `Warranty Duration` | 100.0% | integer:3 | required-like |
| 29 | `Warranty Duration Unit` | 100.0% | string:3 | required-like |

### Other Offerings

Purpose: Technical/service offerings around equipment sales, delivery, refund, reversal and AppleCare-like services. Include selectively or mark as technical. Data rows observed: 17.
Merged/group headers: 2 ranges, sample `Z1:AC1, P1:Y1`.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:17 | required-like |
| 2 | `Design task` | 100.0% | string:17 | required-like |
| 3 | `JSD Ticket` | 23.5% | string:4 | optional/sparse |
| 4 | `Offering Id` | 100.0% | integer:17 | required-like |
| 5 | `External Id` | 100.0% | integer:17 | required-like |
| 6 | `Technical Categories` | 0.0% | blank | empty |
| 7 | `Categories` | 0.0% | blank | empty |
| 8 | `Offering Template` | 100.0% | string:17 | required-like |
| 9 | `Offering Name` | 100.0% | string:17 | required-like |
| 10 | `Display Name` | 100.0% | string:17 | required-like |
| 11 | `Billing Name` | 100.0% | string:17 | required-like |
| 12 | `Offering Group` | 5.9% | string:1 | optional/sparse |
| 13 | `Order Number` | 100.0% | integer:17 | required-like |
| 14 | `Weight` | 0.0% | blank | empty |
| 15 | `Tags` | 76.5% | string:13 | optional/sparse |
| 16 | `Available From` | 100.0% | string:17 | required-like |
| 17 | `Available To` | 0.0% | blank | empty |
| 18 | `Eliminated From` | 0.0% | blank | empty |
| 19 | `Archived From` | 0.0% | blank | empty |
| 20 | `Flat Full Eligibility Condition` | 100.0% | string:17 | required-like |
| 21 | `Is Bundle Only` | 0.0% | blank | empty |
| 22 | `Description` | 0.0% | blank | empty |
| 23 | `Lifecycle Offering Details` | 0.0% | blank | empty |
| 24 | `Product Family` | 29.4% | string:5 | optional/sparse |
| 25 | `Localization Needed` | 0.0% | blank | empty |
| 26 | `SKU ID` | 0.0% | blank | empty |
| 27 | `AppleCare Model` | 0.0% | blank | empty |
| 28 | `Delivery Type` | 5.9% | string:1 | optional/sparse |
| 29 | `AppleCare Duration` | 5.9% | integer:1 | optional/sparse |

### Price List Items

Purpose: Large price fact table joined to products by offering name/template. Best source for catalog price, currency, sale type and pricing options. Data rows observed: 174852.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:174852 | required-like |
| 2 | `Design Task` | 100.0% | string:174852 | required-like |
| 3 | `JSD Ticket` | 87.4% | string:152805 | mostly filled |
| 4 | `Price List Item Id` | 100.0% | integer:174852 | required-like |
| 5 | `Price Key` | 100.0% | integer:174852 | required-like |
| 6 | `Price Key Name` | 0.0% | string:4 | empty |
| 7 | `Offering Template` | 100.0% | string:174852 | required-like |
| 8 | `Offering Name` | 100.0% | string:174852 | required-like |
| 9 | `Price List` | 0.0% | blank | empty |
| 10 | `Price Component Specification` | 100.0% | string:174852 | required-like |
| 11 | `Value` | 100.0% | float:129061, integer:45791 | required-like |
| 12 | `Min Value` | 0.0% | integer:7 | empty |
| 13 | `Max Value` | 0.0% | integer:7 | empty |
| 14 | `Default` | 100.0% | string:174852 | required-like |
| 15 | `Base Price` | 100.0% | string:174852 | required-like |
| 16 | `Tariff Start Date` | 100.0% | string:174852 | required-like |
| 17 | `Tariff End Date` | 0.0% | blank | empty |
| 18 | `Available From` | 100.0% | string:174852 | required-like |
| 19 | `Available To` | 0.0% | string:5 | empty |
| 20 | `Flat Condition Sets` | 98.2% | string:171796 | mostly filled |
| 21 | `Flat Eligibility Conditions` | 0.0% | blank | empty |
| 22 | `Top Offerings` | 98.6% | string:172463 | mostly filled |
| 23 | `Top Categories` | 0.0% | blank | empty |
| 24 | `Threshold` | 0.0% | blank | empty |
| 25 | `Sale Type` | 100.0% | string:174779 | required-like |
| 26 | `Currency` | 100.0% | string:174852 | required-like |
| 27 | `Installment Plan Name` | 60.5% | string:105741 | optional/sparse |
| 28 | `Down Payment` | 0.0% | blank | empty |
| 29 | `Down Payment Min` | 60.5% | integer:78478, float:27263 | optional/sparse |
| 30 | `Down Payment Max` | 60.5% | float:93961, integer:11780 | optional/sparse |
| 31 | `Flat Price Component Variables` | 0.0% | blank | empty |
| 32 | `Revenue Code` | 100.0% | string:174852 | required-like |
| 33 | `Negotiated Price Revenue Code` | 0.0% | string:7 | empty |
| 34 | `Revenue Recognition Class` | 0.0% | blank | empty |
| 35 | `Receivable Class` | 100.0% | string:174852 | required-like |
| 36 | `OTC Id` | 100.0% | integer:174820 | required-like |
| 37 | `Pseudo Event Type Id` | 100.0% | integer:174820 | required-like |
| 38 | `Tariff Id` | 0.0% | integer:30 | optional/sparse |

### Relations Overrides

Purpose: Parent/child relationship overrides. Best source for category-to-product and product-to-product dependencies/exclusions. Data rows observed: 3825.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:3825 | required-like |
| 2 | `Design Task` | 100.0% | string:3825 | required-like |
| 3 | `JSD Ticket` | 98.1% | string:3754 | mostly filled |
| 4 | `Relation Id` | 100.0% | integer:3765, string:60 | required-like |
| 5 | `Relation Type` | 100.0% | string:3825 | required-like |
| 6 | `Parent` | 100.0% | string:3825 | required-like |
| 7 | `Child` | 100.0% | string:3825 | required-like |
| 8 | `Context` | 61.9% | string:2366 | optional/sparse |
| 9 | `Context Type` | 61.9% | string:2366 | optional/sparse |
| 10 | `Min` | 38.1% | integer:1456 | optional/sparse |
| 11 | `Max` | 38.1% | integer:1456 | optional/sparse |
| 12 | `Default` | 100.0% | string:3825 | required-like |
| 13 | `Hide In Details Page` | 30.9% | string:1184 | optional/sparse |
| 14 | `Include Action` | 63.4% | string:2425 | optional/sparse |
| 15 | `Category Count Min` | 0.1% | integer:2 | optional/sparse |
| 16 | `Category Count Max` | 0.1% | integer:2 | optional/sparse |

### Characteristics Overrides

Purpose: Per-offering characteristic visibility/modifiability overrides. Useful as product parameter metadata. Data rows observed: 27.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:27 | required-like |
| 2 | `Design Task` | 100.0% | string:27 | required-like |
| 3 | `JSD Ticket` | 100.0% | string:27 | required-like |
| 4 | `Characteristic Override Id` | 100.0% | integer:27 | required-like |
| 5 | `Flat Offering` | 100.0% | string:27 | required-like |
| 6 | `Characteristic Involvement` | 100.0% | string:27 | required-like |
| 7 | `Visible in CSRD` | 0.0% | blank | empty |
| 8 | `Visible in OE/CPQ` | 100.0% | string:27 | required-like |
| 9 | `Mandatory` | 0.0% | blank | empty |
| 10 | `Modifiable` | 100.0% | string:27 | required-like |

### Available Values

Purpose: Allowed values for configurable characteristics such as sale type and commitment period. Useful for enum extraction. Data rows observed: 1156.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:1156 | required-like |
| 2 | `Design Task` | 100.0% | string:1156 | required-like |
| 3 | `JSD Ticket` | 97.2% | string:1124 | mostly filled |
| 4 | `Flat Offering` | 100.0% | string:1156 | required-like |
| 5 | `Characteristic Involvement` | 100.0% | string:1156 | required-like |
| 6 | `Available Value` | 100.0% | string:1100, integer:56 | required-like |
| 7 | `Disabled From` | 0.0% | blank | empty |

### Flat Discounts

Purpose: Discount definitions and discounted offering/category targeting. Useful for optional promo/discount flags, not core MVP. Data rows observed: 24.
Merged/group headers: 2 ranges, sample `X1:AD1, M1:S1`.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:24 | required-like |
| 2 | `Design Task` | 100.0% | string:24 | required-like |
| 3 | `JSD Ticket` | 100.0% | string:24 | required-like |
| 4 | `Discount Id` | 100.0% | integer:24 | required-like |
| 5 | `Discount Name` | 100.0% | string:24 | required-like |
| 6 | `Display Name` | 100.0% | string:24 | required-like |
| 7 | `Priority` | 100.0% | integer:24 | required-like |
| 8 | `Net Charge` | 100.0% | string:24 | required-like |
| 9 | `Apply Mode` | 100.0% | string:24 | required-like |
| 10 | `Enabled` | 100.0% | string:24 | required-like |
| 11 | `Instance Quantity` | 0.0% | blank | empty |
| 12 | `Tag` | 0.0% | blank | empty |
| 13 | `Price Component Specification #1` | 100.0% | string:24 | required-like |
| 14 | `Discount Amount #1` | 100.0% | float:24 | required-like |
| 15 | `Min Amount #1` | 0.0% | blank | empty |
| 16 | `Max Amount #1` | 0.0% | blank | empty |
| 17 | `Discount Amount Type #1` | 100.0% | string:24 | required-like |
| 18 | `Prorate #1` | 100.0% | string:24 | required-like |
| 19 | `Revenue Code #1` | 100.0% | string:24 | required-like |
| 20 | `Inarrears Only #1` | 0.0% | blank | empty |
| 21 | `Store Qualifying Usage #1` | 0.0% | blank | empty |
| 22 | `Price Alteration Id #1` | 100.0% | integer:22, string:2 | required-like |
| 23 | `Price Alteration #1` | 0.0% | blank | empty |
| 24 | `Price Component Specification #2` | 0.0% | blank | empty |
| 25 | `Discount Amount #2` | 0.0% | blank | empty |
| 26 | `Min Amount #2` | 0.0% | blank | empty |
| 27 | `Max Amount #2` | 0.0% | blank | empty |
| 28 | `Discount Amount Type #2` | 0.0% | blank | empty |
| 29 | `Prorate #2` | 0.0% | blank | empty |
| 30 | `Revenue Code #2` | 0.0% | blank | empty |
| 31 | `Inarrears Only #2` | 0.0% | blank | empty |
| 32 | `Store Qualifying Usage #2` | 0.0% | blank | empty |
| 33 | `Price Alteration Id #2` | 0.0% | blank | empty |
| 34 | `Price Alteration #2` | 0.0% | blank | empty |
| 35 | `Duration` | 0.0% | blank | empty |
| 36 | `Min Duration` | 0.0% | blank | empty |
| 37 | `Max Duration` | 0.0% | blank | empty |
| 38 | `Duration Unit` | 0.0% | blank | empty |
| 39 | `Discounted Flat Offerings` | 100.0% | string:24 | required-like |
| 40 | `Discounted Categories` | 0.0% | blank | empty |
| 41 | `Sale Type` | 100.0% | string:24 | required-like |
| 42 | `Available From` | 100.0% | string:24 | required-like |
| 43 | `Available To` | 0.0% | blank | empty |
| 44 | `Flat Full Eligibility Condition` | 100.0% | string:24 | required-like |
| 45 | `Discount Rules` | 100.0% | string:24 | required-like |
| 46 | `Marketing Description` | 0.0% | blank | empty |
| 47 | `Billing Description` | 0.0% | blank | empty |
| 48 | `Start Date Shift` | 0.0% | blank | empty |
| 49 | `Start Date Shift Unit` | 0.0% | blank | empty |
| 50 | `Manual Start Date Shift` | 0.0% | blank | empty |
| 51 | `Discount Group` | 0.0% | blank | empty |
| 52 | `Permitted to Use` | 0.0% | blank | empty |
| 53 | `Applicable Months` | 0.0% | blank | empty |
| 54 | `Default Applicable Months` | 0.0% | blank | empty |
| 55 | `Applicable Months Editable` | 0.0% | blank | empty |
| 56 | `External Id` | 100.0% | integer:24 | required-like |
| 57 | `Tariff Id` | 100.0% | integer:24 | required-like |
| 58 | `Localization Needed` | 0.0% | blank | empty |
| 59 | `Discount Product Id` | 100.0% | integer:24 | required-like |
| 60 | `Pseudo Event Type Id` | 100.0% | integer:24 | required-like |
| 61 | `DTOF Composite Event Filter Id #1` | 0.0% | blank | empty |
| 62 | `DTOF Charge Event Filter Id #1` | 0.0% | blank | empty |
| 63 | `DTOF Tariff Event Filter Id #1` | 0.0% | blank | empty |
| 64 | `DTOF Event Discount Id #1` | 0.0% | blank | empty |
| 65 | `DTOTC Pseudo Event Type Id #1` | 100.0% | integer:24 | required-like |
| 66 | `DTOTC Event Discount Id #1` | 100.0% | integer:24 | required-like |
| 67 | `DTOF Composite Event Filter Id #2` | 0.0% | blank | empty |
| 68 | `DTOF Charge Event Filter Id #2` | 0.0% | blank | empty |
| 69 | `DTOF Tariff Event Filter Id #2` | 0.0% | blank | empty |
| 70 | `DTOF Event Discount Id #2` | 0.0% | blank | empty |

### Flat Rules

Purpose: Rule definitions for conflicts, discounts and characteristic dependencies. Useful for dependency/condition examples. Data rows observed: 80.
Merged/group headers: 5 ranges, sample `S1:AB1, BB1:BK1, AP1:AY1, H1:Q1, AD1:AM1`.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:80 | required-like |
| 2 | `Design Task` | 100.0% | string:80 | required-like |
| 3 | `JSD Ticket` | 86.2% | string:69 | mostly filled |
| 4 | `Rule Id` | 100.0% | integer:80 | required-like |
| 5 | `Rule Name` | 100.0% | string:80 | required-like |
| 6 | `Rule Type` | 100.0% | string:80 | required-like |
| 7 | `Enabled` | 100.0% | string:80 | required-like |
| 8 | `Offerings/ Categories #1` | 71.2% | string:57 | optional/sparse |
| 9 | `Condition Type #1` | 71.2% | string:57 | optional/sparse |
| 10 | `Characteristic #1` | 27.5% | string:22 | optional/sparse |
| 11 | `Operator #1` | 0.0% | blank | empty |
| 12 | `Characteristic Values #1` | 27.5% | string:19, integer:3 | optional/sparse |
| 13 | `Set of Criteria #1` | 71.2% | string:57 | optional/sparse |
| 14 | `Search Scope #1` | 0.0% | blank | empty |
| 15 | `Custom Condition #1` | 0.0% | blank | empty |
| 16 | `Min Quantity #1` | 0.0% | blank | empty |
| 17 | `Max Quantity #1` | 0.0% | blank | empty |
| 18 | `Counter Type #1` | 0.0% | blank | empty |
| 19 | `Offerings/ Categories #2` | 100.0% | string:80 | required-like |
| 20 | `Condition Type #2` | 100.0% | string:80 | required-like |
| 21 | `Characteristic #2` | 82.5% | string:66 | mostly filled |
| 22 | `Operator #2` | 0.0% | blank | empty |
| 23 | `Characteristic Values #2` | 82.5% | string:56, integer:10 | mostly filled |
| 24 | `Set of Criteria #2` | 100.0% | string:80 | required-like |
| 25 | `Search Scope #2` | 12.5% | string:10 | optional/sparse |
| 26 | `Custom Condition #2` | 2.5% | string:2 | optional/sparse |
| 27 | `Min Quantity #2` | 15.0% | integer:12 | optional/sparse |
| 28 | `Max Quantity #2` | 2.5% | integer:2 | optional/sparse |
| 29 | `Counter Type #2ё` | 0.0% | blank | empty |
| 30 | `Offerings/ Categories #3` | 60.0% | string:48 | optional/sparse |
| 31 | `Condition Type #3` | 60.0% | string:48 | optional/sparse |
| 32 | `Characteristic #3` | 53.8% | string:43 | optional/sparse |
| 33 | `Operator #3` | 0.0% | blank | empty |
| 34 | `Characteristic Values #3` | 53.8% | string:43 | optional/sparse |
| 35 | `Set of Criteria #3` | 60.0% | string:48 | optional/sparse |
| 36 | `Search Scope #3` | 0.0% | blank | empty |
| 37 | `Custom Condition #3` | 1.2% | string:1 | optional/sparse |
| 38 | `Min Quantity #3` | 1.2% | integer:1 | optional/sparse |
| 39 | `Max Quantity #3` | 0.0% | blank | empty |
| 40 | `Counter Type #3` | 0.0% | blank | empty |
| 41 | `Rule Condition 3` | 0.0% | blank | empty |
| 42 | `Offerings/ Categories #4` | 38.8% | string:31 | optional/sparse |
| 43 | `Condition Type #4` | 38.8% | string:31 | optional/sparse |
| 44 | `Characteristic #4` | 35.0% | string:28 | optional/sparse |
| 45 | `Operator #4` | 0.0% | blank | empty |
| 46 | `Characteristic Values #4` | 35.0% | integer:22, string:6 | optional/sparse |
| 47 | `Set of Criteria #4` | 38.8% | string:31 | optional/sparse |
| 48 | `Search Scope #4` | 2.5% | string:2 | optional/sparse |
| 49 | `Custom Condition #4` | 5.0% | string:4 | optional/sparse |
| 50 | `Min Quantity #4` | 0.0% | blank | empty |
| 51 | `Max Quantity #4` | 0.0% | blank | empty |
| 52 | `Counter Type #4` | 0.0% | blank | empty |
| 53 | `Rule Condition 4` | 0.0% | blank | empty |
| 54 | `Offerings/ Categories #5` | 0.0% | blank | empty |
| 55 | `Condition Type #5` | 0.0% | blank | empty |
| 56 | `Characteristic #5` | 0.0% | blank | empty |
| 57 | `Operator #5` | 0.0% | blank | empty |
| 58 | `Characteristic Values #5` | 0.0% | blank | empty |
| 59 | `Set of Criteria #5` | 0.0% | blank | empty |
| 60 | `Search Scope #5` | 0.0% | blank | empty |
| 61 | `Custom Condition #5` | 0.0% | blank | empty |
| 62 | `Min Quantity #5` | 0.0% | blank | empty |
| 63 | `Max Quantity #5` | 0.0% | blank | empty |
| 64 | `Counter Type #5` | 0.0% | blank | empty |
| 65 | `Rule Condition 5` | 0.0% | blank | empty |
| 66 | `Search Scope` | 100.0% | string:80 | required-like |
| 67 | `Rule Action` | 61.3% | string:49 | optional/sparse |
| 68 | `Auto-applied Offerings` | 1.2% | integer:1 | optional/sparse |
| 69 | `Removal Action` | 0.0% | blank | empty |
| 70 | `Installation Rule Type` | 28.7% | string:23 | optional/sparse |
| 71 | `Action Message` | 61.3% | string:49 | optional/sparse |
| 72 | `Custom Action` | 0.0% | blank | empty |
| 73 | `Custom Condition` | 0.0% | blank | empty |
| 74 | `Flat Full Eligibility Condition` | 1.2% | string:1 | optional/sparse |
| 75 | `Localization Needed` | 40.0% | string:32 | optional/sparse |

### Complex Flat Rules

Purpose: Small set of multi-condition dependency rules. Good source for demo edge cases. Data rows observed: 5.
Merged/group headers: 4 ranges, sample `H1:T1, AQ1:AX1, AH1:AO1, U1:AG1`.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:5 | required-like |
| 2 | `Design task` | 100.0% | string:5 | required-like |
| 3 | `JSD Ticket` | 80.0% | string:4 | mostly filled |
| 4 | `Rule Id` | 100.0% | integer:5 | required-like |
| 5 | `Rule Name` | 100.0% | string:5 | required-like |
| 6 | `Rule Type` | 100.0% | string:5 | required-like |
| 7 | `Enabled` | 100.0% | string:5 | required-like |
| 8 | `Offerings/ Categories #1` | 100.0% | string:5 | required-like |
| 9 | `Condition Type #1` | 100.0% | string:5 | required-like |
| 10 | `Characteristic #1` | 0.0% | blank | empty |
| 11 | `Characteristic Values #1` | 0.0% | blank | empty |
| 12 | `Operator #1` | 0.0% | blank | empty |
| 13 | `Set of Criteria #1` | 100.0% | string:5 | required-like |
| 14 | `Search Scope #1` | 0.0% | blank | empty |
| 15 | `Counter Type #1` | 0.0% | blank | empty |
| 16 | `Min Quantity #1` | 0.0% | blank | empty |
| 17 | `Max Quantity #1` | 0.0% | blank | empty |
| 18 | `Target For Apply #1` | 0.0% | blank | empty |
| 19 | `Is Precondition #1` | 100.0% | string:5 | required-like |
| 20 | `Custom Condition #1` | 0.0% | blank | empty |
| 21 | `Offerings/ Categories #2` | 100.0% | string:5 | required-like |
| 22 | `Condition Type #2` | 100.0% | string:5 | required-like |
| 23 | `Characteristic #2` | 100.0% | string:5 | required-like |
| 24 | `Characteristic Values #2` | 100.0% | string:5 | required-like |
| 25 | `Operator #2` | 0.0% | blank | empty |
| 26 | `Set of Criteria #2` | 100.0% | string:5 | required-like |
| 27 | `Search Scope #2` | 0.0% | blank | empty |
| 28 | `Counter Type #2` | 0.0% | blank | empty |
| 29 | `Min Quantity #2` | 60.0% | integer:3 | optional/sparse |
| 30 | `Max Quantity #2` | 0.0% | blank | empty |
| 31 | `Target For Apply #2` | 0.0% | blank | empty |
| 32 | `Is Precondition #2` | 100.0% | string:5 | required-like |
| 33 | `Custom Condition #2` | 0.0% | blank | empty |
| 34 | `Offerings/ Categories #3` | 100.0% | string:5 | required-like |
| 35 | `Condition Type #3` | 100.0% | string:5 | required-like |
| 36 | `Characteristic #3` | 100.0% | string:5 | required-like |
| 37 | `Characteristic Values #3` | 100.0% | string:5 | required-like |
| 38 | `Set of Criteria #3` | 100.0% | string:5 | required-like |
| 39 | `Search Scope #3` | 0.0% | blank | empty |
| 40 | `Is Precondition #3` | 0.0% | blank | empty |
| 41 | `Custom Condition #3` | 20.0% | string:1 | optional/sparse |
| 42 | `Rule Condition 3` | 0.0% | blank | empty |
| 43 | `Offerings/ Categories #4` | 0.0% | blank | empty |
| 44 | `Condition Type #4` | 0.0% | blank | empty |
| 45 | `Characteristic #4` | 0.0% | blank | empty |
| 46 | `Characteristic Values #4` | 0.0% | blank | empty |
| 47 | `Set of Criteria #4` | 0.0% | blank | empty |
| 48 | `Search Scope #4` | 0.0% | blank | empty |
| 49 | `Is Precondition #4` | 0.0% | blank | empty |
| 50 | `Custom Condition #4` | 0.0% | blank | empty |
| 51 | `Rule Condition 4` | 0.0% | blank | empty |
| 52 | `Search Scope` | 100.0% | string:5 | required-like |
| 53 | `Rule Action` | 100.0% | string:5 | required-like |
| 54 | `Auto-applied Offerings` | 0.0% | blank | empty |
| 55 | `Removal Action` | 0.0% | blank | empty |
| 56 | `Installation Rule Type` | 0.0% | blank | empty |
| 57 | `Action Message` | 100.0% | string:5 | required-like |
| 58 | `Custom Action` | 0.0% | blank | empty |
| 59 | `Custom Condition` | 0.0% | blank | empty |
| 60 | `Localization Needed` | 0.0% | blank | empty |

### Characteristic State Rules

Purpose: Rules that change characteristic visibility/mandatory/modifiable state. Useful for advanced training scenarios. Data rows observed: 12.
Merged/group headers: 2 ranges, sample `H1:L1, M1:R1`.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:12 | required-like |
| 2 | `Design Task` | 100.0% | string:12 | required-like |
| 3 | `JSD Ticket` | 25.0% | string:3 | optional/sparse |
| 4 | `Rule Id` | 100.0% | integer:12 | required-like |
| 5 | `Rule Name` | 100.0% | string:12 | required-like |
| 6 | `Rule Type` | 100.0% | string:12 | required-like |
| 7 | `Enabled` | 100.0% | string:12 | required-like |
| 8 | `Offerings/ Categories #1` | 100.0% | string:12 | required-like |
| 9 | `Condition Type #1` | 100.0% | string:12 | required-like |
| 10 | `Characteristic #1` | 58.3% | string:7 | optional/sparse |
| 11 | `Characteristic Values #1` | 58.3% | string:7 | optional/sparse |
| 12 | `Set of Criteria #1` | 100.0% | string:12 | required-like |
| 13 | `Offerings/ Categories` | 100.0% | string:12 | required-like |
| 14 | `Type` | 100.0% | string:12 | required-like |
| 15 | `Characteristic` | 100.0% | string:12 | required-like |
| 16 | `Visible` | 100.0% | string:12 | required-like |
| 17 | `Mandatory` | 83.3% | string:10 | mostly filled |
| 18 | `Modifiable` | 100.0% | string:12 | required-like |
| 19 | `Search Scope` | 100.0% | string:12 | required-like |
| 20 | `Notification Message` | 100.0% | string:12 | required-like |
| 21 | `Localization Needed` | 100.0% | string:12 | required-like |

### Prices

Purpose: Template-level price component configuration. Secondary source; not needed for MVP product list. Data rows observed: 2.
Merged/group headers: 8 ranges, sample `D1:L1, AY1:BC1, AJ1:AL1, R1:X1, AB1:AI1, Z1:AA1`.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 100.0% | string:2 | required-like |
| 2 | `Design Task` | 100.0% | string:2 | required-like |
| 3 | `JSD Ticket` | 50.0% | string:1 | optional/sparse |
| 4 | `Template` | 0.0% | blank | empty |
| 5 | `parent Product Offering` | 100.0% | string:2 | required-like |
| 6 | `OPC Name` | 0.0% | blank | empty |
| 7 | `Market` | 0.0% | blank | empty |
| 8 | `Customer Category` | 0.0% | blank | empty |
| 9 | `Distribution Channel` | 0.0% | blank | empty |
| 10 | `Currency Name` | 0.0% | blank | empty |
| 11 | `Price Component Specification` | 0.0% | blank | empty |
| 12 | `Prepayment required` | 0.0% | blank | empty |
| 13 | `External Event Specification` | 0.0% | blank | empty |
| 14 | `PCV Characteristic` | 0.0% | blank | empty |
| 15 | `Price Variable Unit Measure` | 0.0% | blank | empty |
| 16 | `Calculation Mode` | 0.0% | blank | empty |
| 17 | `Merge Price Plans` | 0.0% | blank | empty |
| 18 | `PN Name #1` | 0.0% | blank | empty |
| 19 | `PN Type #1` | 0.0% | blank | empty |
| 20 | `PN Characteristic #1` | 0.0% | blank | empty |
| 21 | `Characteristic Values #1` | 0.0% | blank | empty |
| 22 | `Price Interval #1` | 0.0% | blank | empty |
| 23 | `Is Base Price #1` | 0.0% | blank | empty |
| 24 | `Migration ID for PN #1` | 0.0% | blank | empty |
| 25 | `Rate Plans` | 0.0% | blank | empty |
| 26 | `Multiplier` | 0.0% | blank | empty |
| 27 | `Calculated Multiplier` | 0.0% | blank | empty |
| 28 | `No Charge` | 0.0% | blank | empty |
| 29 | `Min Value` | 0.0% | blank | empty |
| 30 | `Value` | 0.0% | blank | empty |
| 31 | `Max Value` | 0.0% | blank | empty |
| 32 | `PV Start Date` | 0.0% | blank | empty |
| 33 | `PV End Date` | 0.0% | blank | empty |
| 34 | `Installment Plan` | 100.0% | string:2 | required-like |
| 35 | `Custom Logic` | 0.0% | blank | empty |
| 36 | `Revenue Code` | 0.0% | blank | empty |
| 37 | `Negotiated Price Revenue Code` | 0.0% | blank | empty |
| 38 | `Receivable Class` | 0.0% | blank | empty |
| 39 | `Down Payment` | 0.0% | blank | empty |
| 40 | `Down Payment Min` | 0.0% | blank | empty |
| 41 | `Down Payment Max` | 0.0% | blank | empty |
| 42 | `Down Payment Type` | 100.0% | string:2 | required-like |
| 43 | `Installment Technical OTS` | 100.0% | string:2 | required-like |
| 44 | `Price Key Name of the Installment Technical One-Time Service` | 100.0% | string:2 | required-like |
| 45 | `Refund Installment Technical OTS` | 100.0% | string:2 | required-like |
| 46 | `Price Key Name of the Refund Installment Technical One-Time Service` | 100.0% | string:2 | required-like |
| 47 | `Refund Reverse Adjustment Type` | 100.0% | string:2 | required-like |
| 48 | `Reversal OTS` | 100.0% | string:2 | required-like |
| 49 | `Installment Period` | 100.0% | integer:2 | required-like |
| 50 | `Installment Period Unit` | 100.0% | string:2 | required-like |
| 51 | `Down Payment Revenue Code` | 100.0% | string:2 | required-like |
| 52 | `One-Time Service Price Node` | 0.0% | blank | empty |
| 53 | `One-Time Service Price` | 0.0% | blank | empty |
| 54 | `Installment Taxation` | 100.0% | string:2 | required-like |
| 55 | `Charge Reverse Adjustment Type` | 100.0% | string:2 | required-like |
| 56 | `ID For Migration Stream` | 100.0% | integer:2 | required-like |

### Price Variables

Purpose: Declared price-variable columns but no actual data rows. Treat as placeholder/service sheet. Data rows observed: 0.

| # | Column | Fill | Types | Required guess |
|---:|---|---:|---|---|
| 1 | `Iteration` | 0.0% | blank | empty |
| 2 | `Price Component Variable Id` | 0.0% | blank | empty |
| 3 | `Variable Name` | 0.0% | blank | empty |
| 4 | `Offering Characteristic` | 0.0% | blank | empty |
| 5 | `Price Variable Unit Measure` | 0.0% | blank | empty |
| 6 | `Calculation Mode` | 0.0% | blank | empty |
| 7 | `Merge Tariffs` | 0.0% | blank | empty |

## 4. Assumed purpose of each sheet

- `Cover Page`: Cover/status page. Not a normalized catalog table; useful only as source documentation. Rows used for analysis: 2.
- `Revision History`: Change log with versions, dates, authors/reasons and internal ticket references. Useful for audit only, not for demo runtime catalog. Rows used for analysis: 635.
- `Offerings`: Master configuration for categories, templates, relations, characteristics and price-component metadata. Too wide for direct portal display; useful for taxonomy and parameter definitions. Rows used for analysis: 725.
- `Equipment Offerings`: Primary catalog product sheet for serialized equipment items. Best MVP product source. Rows used for analysis: 2279.
- `Non-serialized Equipment`: Small product sheet for non-serialized accessories. Same shape as equipment; include in MVP as product source. Rows used for analysis: 3.
- `Other Offerings`: Technical/service offerings around equipment sales, delivery, refund, reversal and AppleCare-like services. Include selectively or mark as technical. Rows used for analysis: 17.
- `Price List Items`: Large price fact table joined to products by offering name/template. Best source for catalog price, currency, sale type and pricing options. Rows used for analysis: 174852.
- `Relations Overrides`: Parent/child relationship overrides. Best source for category-to-product and product-to-product dependencies/exclusions. Rows used for analysis: 3825.
- `Characteristics Overrides`: Per-offering characteristic visibility/modifiability overrides. Useful as product parameter metadata. Rows used for analysis: 27.
- `Available Values`: Allowed values for configurable characteristics such as sale type and commitment period. Useful for enum extraction. Rows used for analysis: 1156.
- `Flat Discounts`: Discount definitions and discounted offering/category targeting. Useful for optional promo/discount flags, not core MVP. Rows used for analysis: 24.
- `Flat Rules`: Rule definitions for conflicts, discounts and characteristic dependencies. Useful for dependency/condition examples. Rows used for analysis: 80.
- `Complex Flat Rules`: Small set of multi-condition dependency rules. Good source for demo edge cases. Rows used for analysis: 5.
- `Characteristic State Rules`: Rules that change characteristic visibility/mandatory/modifiable state. Useful for advanced training scenarios. Rows used for analysis: 12.
- `Prices`: Template-level price component configuration. Secondary source; not needed for MVP product list. Rows used for analysis: 2.
- `Price Variables`: Declared price-variable columns but no actual data rows. Treat as placeholder/service sheet. Rows used for analysis: 0.

## 5. Catalog entities found

| Entity | Source sheet(s) | Identifier | Notes |
|---|---|---|---|
| Product/offering | `Equipment Offerings`, `Non-serialized Equipment`, `Other Offerings` | `Offering Id`, fallback `External Id`/normalized name | Primary UI entity. Source rows contain display name, template, family, tags, availability and traceability. |
| Category | `Offerings`, `Relations Overrides`, product `Categories`/`Technical Categories` columns | normalized category name | Category hierarchy is implicit, not a clean normalized table. |
| Price option | `Price List Items`, secondary `Prices` | `Price List Item Id` / `Price Key` | Multiple price rows can exist per product by sale type, installment plan, base/default flags and validity dates. |
| Characteristic/parameter | `Offerings`, `Available Values`, `Characteristics Overrides`, `Characteristic State Rules` | characteristic name + offering name | Used as product parameters and configurable values. |
| Dependency/relation | `Relations Overrides`, `Flat Rules`, `Complex Flat Rules` | `Relation Id` / `Rule Id` | Captures includes/excludes/requires/conflicts. |
| Discount/promotion | `Flat Discounts`, `Flat Rules` | `Discount Id` / `Rule Id` | Optional MVP extension. Good training data for edge cases. |
| Audit/source metadata | every data sheet | sheet name + row number | Required for traceability in the demo UI/API. |

## 6. Relationships between entities

- Product price relationship: product `Offering Name` joins to `Price List Items.Offering Name`. Coverage is high but not perfect.
- Price coverage: 2293 of 2299 product names have at least one price row; 6 product names do not.
- Category/product relationship: `Relations Overrides` contains `c2o`, `o2c`, and `o2o` relation types. These should be normalized to dependencies with `relationType`, `parent`, `child`, `min`, `max`, `includeAction`, `defaultSelected`.
- Characteristic relationship: `Available Values` binds `Flat Offering` + `Characteristic Involvement` to allowed values. `Characteristics Overrides` and `Characteristic State Rules` override visibility/mandatory/modifiable behavior.
- Rule relationship: `Flat Rules` and `Complex Flat Rules` reference offerings/categories and characteristics across repeated condition groups. For MVP they should become simplified dependency/condition records, not a full rule engine.

Relation type distribution:
- `c2o`: 2369
- `o2o`: 1301
- `o2c`: 155

## 7. Possible enum values

| Enum | Observed values / proposed normalized values |
|---|---|
| Source iteration | Observed release-like markers such as production/config/delete markers. For demo: `PROD`, `CONFIG`, `DELETE`, `DRAFT`. |
| Offering type | Observed from `Offerings.Type`: category, category-to-offering, offering-to-offering, template offering, characteristic, price detail. For demo: `DEVICE`, `ACCESSORY`, `SERVICE`, `TECHNICAL`, `BUNDLE`. |
| Product family | Observed families include smartphone, accessory, smartwatch, tablet, router/WiFi, extender, gaming device. For demo: `SMARTPHONE`, `TABLET`, `SMARTWATCH`, `ROUTER`, `ACCESSORY`, `SERVICE`. |
| Status | Not explicit as a single column. Derived from `Available From`, `Available To`, `Eliminated From`, `Archived From`: `ACTIVE`, `EXPIRED`, `FUTURE`, `ELIMINATED`, `ARCHIVED`, `INVALID`. |
| Sale type | Observed values: `Cash Sales`, `Installments`, `Subsidy`. For demo keep these as `CASH`, `INSTALLMENT`, `SUBSIDY`. |
| Currency | Source has one local currency value. For demo use neutral `USD` or configurable `LOCAL`. |
| Boolean-like fields | Observed mixed `Yes`/`No`, booleans, and uppercase/lowercase strings. Normalize to boolean. |
| Rule type | Observed: conflicts, requires, discount, characteristic dependency, characteristic state. For demo: `CONFLICTS_WITH`, `REQUIRES`, `DISCOUNT`, `CHARACTERISTIC_DEPENDENCY`, `STATE_CHANGE`. |
| Include action | Observed: `Include`, `Exclude`, blank. For demo: `INCLUDE`, `EXCLUDE`, `NONE`. |

## 8. Fields needed by frontend

| UI need | Field(s) | Source/derivation |
|---|---|---|
| Catalog card/table identity | `id`, `code`, `name` | `Offering Id`, `External Id`, `Display Name`/`Offering Name`. |
| Search | `name`, `code`, `description`, `category`, `type`, `parameters` | Normalized from product rows and selected characteristics. |
| Filters | `category`, `status`, `type`, `price`, `currency` | Product sheets + derived status + price aggregation. |
| Price display | `price`, `currency`, `period`, `pricingOptions` | `Price List Items.Value`, `Currency`, sale type/installment metadata. |
| Product details | `description`, `parameters`, `conditions`, `dependencies` | Product descriptions, characteristic sheets, relations/rules. |
| Traceability | `sourceFile`, `sourceSheet`, `sourceRow`, `importedAt` | Importer-generated metadata. |
| Data quality states | `validationIssues`, `warnings` | Import validation. |

## 9. Fields needed by backend

- Import configuration: input Excel path, output JSON path, active sheet list, row header strategy, currency fallback, timezone.
- Normalized product fields: `id`, `code`, `name`, `description`, `category`, `type`, `status`, `price`, `currency`, `period`, `parameters`, `conditions`, `dependencies`, `sourceSheet`, `sourceRow`, `sourceFile`, `importedAt`, `validationIssues`.
- Metadata fields: import timestamp, source workbook fingerprint, source sheets, counts, enum values, available filters, warning count.
- Search/filter fields: lower-cased search index, numeric price, status, type, category, active flags.
- Traceability fields: each imported product and generated dependency must carry sheet/row/source file information.

## 10. Fields that look service/internal

- Change management: `Design Task`, `JSD Ticket`, revision authors/reasons.
- Migration/system IDs: `ID For Migration Stream`, `Price Key`, technical event IDs, pseudo event IDs, tariff IDs, event filter IDs.
- Billing/provisioning internals: revenue codes, receivable classes, event specifications, OM mapping, product component mapping.
- UI/backend demo should preserve these only as optional `rawAttributes` or omit them entirely. Do not expose source customer-specific identifiers in the demo portal.

## 11. Data quality issues

- Source workbook contains customer-specific branding, ticket references, localized labels and operational identifiers. A sanitized demo workbook must be generated before implementation.
- Header model is not uniform: row 1 often contains grouped labels or migration IDs; row 2 is the real header for most sheets.
- Wide sparse sheets: `Offerings` has 186 columns with many empty or near-empty fields. Importer should use a whitelist, not a blind full-column mapping.
- Large fact table: `Price List Items` has 174,852 data rows. Runtime Excel parsing would be slow and unnecessary for the demo.
- Status is implicit, not explicit. It must be derived from availability/archive/elimination dates.
- Date values are represented as strings in `dd.MM.yyyy` style in key sheets. Importer must parse and validate this explicitly.
- Boolean values are inconsistent: some sheets use `Yes`/`No`, others contain real booleans or uppercase/lowercase strings.
- Price coverage is almost complete but not perfect: 6 product names do not have matching price rows.
- Negative price values appear for refund/reversal technical offerings. For the portal MVP, these should either be hidden as technical items or shown as validation warnings.
- Some product rows have missing product family values, especially technical/other offerings.
- One header contains a typo/non-standard character: `Counter Type #2ё` in `Flat Rules`. Importer should not rely on exact text for repeated rule groups without normalization.
- `Price Variables` is effectively an empty placeholder sheet.

## 12. Open questions

1. Confirm the sanitized demo operator name. Proposed: `DemoTel` or `Training Mobile`.
2. Confirm the demo currency. Proposed: `USD` to keep the project neutral.
3. Confirm whether technical/refund/reversal offerings should be hidden from the main catalog by default.
4. Runtime catalog import is intentionally omitted for MVP. Use the Maven/import command to regenerate normalized JSON from Excel.
5. Confirm frontend language. Proposed: English UI with i18next prepared for Russian/English labels, to keep code examples broadly readable.
6. Confirm whether the source workbook should remain in repo root or be ignored after the sanitized workbook is created.

## 13. Proposed domain model

```text
CatalogDocument
  metadata: CatalogMetadata
  products: CatalogProduct[]
  categories: CatalogCategory[]
  enums: CatalogEnums
  warnings: ValidationIssue[]

CatalogProduct
  id: string
  code: string
  name: string
  description?: string
  category: string
  type: ProductType
  status: ProductStatus
  price?: decimal
  currency?: string
  period?: string
  pricingOptions: PriceOption[]
  parameters: ProductParameter[]
  conditions: string[]
  dependencies: ProductDependency[]
  sourceSheet: string
  sourceRow: number
  sourceFile: string
  importedAt: OffsetDateTime
  validationIssues: ValidationIssue[]

PriceOption
  id: string
  component: string
  saleType: CASH | INSTALLMENT | SUBSIDY | OTHER
  value: decimal
  currency: string
  defaultPrice: boolean
  basePrice: boolean
  period?: string

ProductDependency
  type: INCLUDE | EXCLUDE | REQUIRES | CONFLICTS_WITH
  targetId?: string
  targetName: string
  min?: number
  max?: number
  sourceSheet: string
  sourceRow: number
```

Import validation approach: skip only rows that cannot produce `id` and `name`; include rows with non-critical problems and attach `validationIssues`. This is better for training because learners can see imperfect catalog data in UI/API without hiding all edge cases.

## 14. Proposed API contract

### GET /api/catalog/products

Query params: `search`, `category`, `status`, `type`, `priceFrom`, `priceTo`, `page`, `pageSize`.

Response shape:

```json
{
  "items": [
    {
      "id": "demo-phone-pro-256",
      "code": "DMP-256",
      "name": "iPhone 14 Midnight 128GB",
      "category": "Smartphones",
      "type": "SMARTPHONE",
      "status": "ACTIVE",
      "price": 899.0,
      "currency": "USD",
      "period": "one-time",
      "shortDescription": "Apple smartphone offer sourced from the sanitized Excel catalog",
      "sourceSheet": "Equipment Offerings",
      "sourceRow": 3
    }
  ],
  "page": 0,
  "pageSize": 20,
  "total": 1
}
```

### GET /api/catalog/products/{id}

Returns full `CatalogProduct` with parameters, conditions, dependencies, source info and validation issues.

### GET /api/catalog/categories

Returns normalized categories with counts and optional parent/child information.

### GET /api/catalog/metadata

Returns import timestamp, source file name, source sheets, product count, warning count, filters and enum values.

Runtime `POST /api/catalog/import` is out of scope for the MVP. Catalog import is done by the Maven/script command that generates `catalog/generated/catalog.json`.

## 15. Proposed UI structure

- Route `/catalog`: catalog page with search, filters, metadata strip, and product table/cards.
- Route `/catalog/:id`: product details page or drawer backed by route state. For training, a route is preferable because it exercises router and API loading states.
- Catalog metadata block: import date, product count, source workbook, source sheets, categories, statuses and warning count.
- Product list: name, code, category, type, status badge, price, short description, source sheet/row.
- Details: product identity, full description, price options, parameters, conditions, dependencies, validation issues, source traceability.
- Required states: loading, empty search result, product not found, backend unavailable, invalid catalog data warning.

## 16. Proposed repository structure

```text
backend/
  pom.xml
  catalog-parent/
    pom.xml
  catalog-api/
    pom.xml
    src/main/java/.../dto
    src/main/java/.../resource
  catalog-core/
    pom.xml
    src/main/java/.../model
    src/main/java/.../service
    src/main/java/.../mapper
  catalog-import/
    pom.xml
    src/main/java/.../importer
    src/test/java/...
  catalog-app/
    pom.xml
    src/main/resources/application.yaml
    src/main/resources/catalog/generated/catalog.json
    src/test/java/...

frontend/
  package.json
  package-lock.json
  config-overrides.js
  src/
    app/
    api/
    components/
    pages/
    i18n/
    styles/

catalog/
  mobile-operator-catalog.xlsx
  generated/catalog.json

docs/
  catalog-analysis.md
  ai-training-guide.md

README.md
AGENTS.md
```

## 17. Implementation plan after approval

1. Create sanitized `catalog/mobile-operator-catalog.xlsx` with neutral operator/product names and a small curated subset inspired by the source structure.
2. Generate `catalog/generated/catalog.json` from the sanitized workbook.
3. Create Maven multi-module Quarkus backend with API/core/import/app modules.
4. Implement importer, validation warnings and traceability fields.
5. Implement REST endpoints and OpenAPI/health/metrics configuration.
6. Add backend tests for import transform, filtering/search, API list/detail and traceability.
7. Create CRA React frontend with React Router, Redux Toolkit, RTK Query, Ant Design, Less and i18next.
8. Add README, AGENTS.md and docs/ai-training-guide.md.
9. Run build/test, fix errors and provide final execution summary.
