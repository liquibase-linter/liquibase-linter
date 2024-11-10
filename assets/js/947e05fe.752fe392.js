"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[3726],{4684:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>d,contentTitle:()=>o,default:()=>h,frontMatter:()=>r,metadata:()=>i,toc:()=>l});const i=JSON.parse('{"id":"rules/modify-data-enforce-where","title":"modify-data-enforce-where","description":"Why?","source":"@site/docs/rules/modify-data-enforce-where.md","sourceDirName":"rules","slug":"/rules/modify-data-enforce-where","permalink":"/liquibase-linter/docs/rules/modify-data-enforce-where","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{"title":"modify-data-enforce-where"},"sidebar":"docs","previous":{"title":"isolate-ddl-changes","permalink":"/liquibase-linter/docs/rules/isolate-ddl-changes"},"next":{"title":"modify-data-starts-with-where","permalink":"/liquibase-linter/docs/rules/modify-data-starts-with-where"}}');var a=t(4848),s=t(8453);const r={title:"modify-data-enforce-where"},o=void 0,d={},l=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2},{value:"Example Usage",id:"example-usage",level:2}];function c(e){const n={code:"code",h2:"h2",li:"li",p:"p",pre:"pre",ul:"ul",...(0,s.R)(),...e.components};return(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(n.h2,{id:"why",children:"Why?"}),"\n",(0,a.jsxs)(n.p,{children:["When updating or deleting data in a table, it's possible to do an unqualified modification; that is, not provided a ",(0,a.jsx)(n.code,{children:"<where>"})," condition so that all rows are updated/deleted."]}),"\n",(0,a.jsx)(n.p,{children:"Whilst this can be a valid usage, it is prone to mistakes, and there might be some tables on which an oversight could be particularly harmful, like those with multi-tenant (or otherwise scoped) data."}),"\n",(0,a.jsxs)(n.p,{children:["Additionally, even qualified updates on certain tables can be problematic if they are not differentiating on a particular column or columns, so you can get more specific about this by providing a regular expression that any ",(0,a.jsx)(n.code,{children:"<where>"})," condition must adhere to."]}),"\n",(0,a.jsxs)(n.p,{children:["This rule will fail if an update or delete change on any of the given tables does not include a ",(0,a.jsx)(n.code,{children:"<where>"})," condition, or if a ",(0,a.jsx)(n.code,{children:"<where>"})," condition is included but does not match a specified pattern."]}),"\n",(0,a.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,a.jsxs)(n.ul,{children:["\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.code,{children:"values"})," - (array of regex strings) list of table name patterns that cannot have unqualified modifications"]}),"\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.code,{children:"pattern"})," - (regex, as string) optional regular expression that ",(0,a.jsx)(n.code,{children:"<where>"})," conditions must adhere to"]}),"\n"]}),"\n",(0,a.jsx)(n.h2,{id:"example-usage",children:"Example Usage"}),"\n",(0,a.jsx)(n.p,{children:"A basic usage:"}),"\n",(0,a.jsx)(n.pre,{children:(0,a.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "modify-data-enforce-where": {\n            "values": ["SETTINGS"],\n            "errorMessage": "Updates and deletes to settings table must have a where condition"\n        }\n    }\n}\n'})}),"\n",(0,a.jsx)(n.p,{children:"With the addition of a pattern:"}),"\n",(0,a.jsx)(n.pre,{children:(0,a.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "modify-data-enforce-where": {\n            "values": ["SETTINGS"],\n            "pattern": "^.*GROUP =.*$",\n            "errorMessage": "Updates and deletes to settings table must have a where condition that references group column"\n        }\n    }\n}\n'})})]})}function h(e={}){const{wrapper:n}={...(0,s.R)(),...e.components};return n?(0,a.jsx)(n,{...e,children:(0,a.jsx)(c,{...e})}):c(e)}},8453:(e,n,t)=>{t.d(n,{R:()=>r,x:()=>o});var i=t(6540);const a={},s=i.createContext(a);function r(e){const n=i.useContext(s);return i.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function o(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(a):e.components||a:r(e.components),i.createElement(s.Provider,{value:n},e.children)}}}]);