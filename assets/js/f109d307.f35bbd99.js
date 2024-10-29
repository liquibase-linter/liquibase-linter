"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[3161],{6537:(e,n,a)=>{a.r(n),a.d(n,{assets:()=>o,contentTitle:()=>s,default:()=>h,frontMatter:()=>t,metadata:()=>l,toc:()=>c});var i=a(4848),r=a(8453);const t={title:"primary-key-name"},s=void 0,l={id:"rules/primary-key-name",title:"primary-key-name",description:"Why?",source:"@site/docs/rules/primary-key-name.md",sourceDirName:"rules",slug:"/rules/primary-key-name",permalink:"/liquibase-linter/docs/rules/primary-key-name",draft:!1,unlisted:!1,tags:[],version:"current",frontMatter:{title:"primary-key-name"},sidebar:"docs",previous:{title:"object-name-length",permalink:"/liquibase-linter/docs/rules/object-name-length"},next:{title:"schema-name",permalink:"/liquibase-linter/docs/rules/schema-name"}},o={},c=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2},{value:"Example Usage",id:"example-usage",level:2}];function d(e){const n={a:"a",code:"code",h2:"h2",li:"li",p:"p",pre:"pre",ul:"ul",...(0,r.R)(),...e.components};return(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(n.h2,{id:"why",children:"Why?"}),"\n",(0,i.jsxs)(n.p,{children:["When ",(0,i.jsx)(n.a,{href:"http://www.liquibase.org/documentation/changes/add_primary_key.html",children:"adding a primary key"}),", it's possible to omit a specific ",(0,i.jsx)(n.code,{children:"constraintName"})," for the primary key. This is hazardous, because the database vendor will automatically name it for you in an unpredictable way, which makes things difficult later if you want to reference or remove it."]}),"\n",(0,i.jsxs)(n.p,{children:["Also, you might already have a broad standard for object names - and be enforcing it with ",(0,i.jsx)(n.a,{href:"/liquibase-linter/docs/rules/object-name",children:"the object-name rule"})," - but you might also want a more specific rule concerning how primary keys are named."]}),"\n",(0,i.jsxs)(n.p,{children:["This rule will fail if there is no ",(0,i.jsx)(n.code,{children:"constraintName"})," given when adding a primary key, or when configured with a pattern, will fail if the given name does not match the pattern."]}),"\n",(0,i.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"pattern"})," - (regex, as string) optional regular expression that the name of any added primary key must adhere to"]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"dynamicValue"})," - (string) Spring EL expression, with the ",(0,i.jsx)(n.a,{href:"https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/change/core/AddPrimaryKeyChange.java",children:(0,i.jsx)(n.code,{children:"AddPrimaryKeyChange"})})," instance as its expression scope, that should resolve to a string, and can then be interpolated in the pattern with ",(0,i.jsx)(n.code,{children:"{{value}}"})]}),"\n"]}),"\n",(0,i.jsx)(n.h2,{id:"example-usage",children:"Example Usage"}),"\n",(0,i.jsx)(n.p,{children:"To simply ensure that a name is always given:"}),"\n",(0,i.jsx)(n.pre,{children:(0,i.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "primary-key-name": true\n    }\n}\n'})}),"\n",(0,i.jsx)(n.p,{children:"To ensure that a pattern is matched, including the table name:"}),"\n",(0,i.jsx)(n.pre,{children:(0,i.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "primary-key-name": {\n            "pattern": "^{{value}}_PK$",\n            "dynamicValue": "tableName",\n            "errorMessage": "Primary key names must be the table name, suffixed with \'PK\', e.g. FOO_PK"\n        }\n    }\n}\n'})})]})}function h(e={}){const{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,i.jsx)(n,{...e,children:(0,i.jsx)(d,{...e})}):d(e)}},8453:(e,n,a)=>{a.d(n,{R:()=>s,x:()=>l});var i=a(6540);const r={},t=i.createContext(r);function s(e){const n=i.useContext(t);return i.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function l(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(r):e.components||r:s(e.components),i.createElement(t.Provider,{value:n},e.children)}}}]);