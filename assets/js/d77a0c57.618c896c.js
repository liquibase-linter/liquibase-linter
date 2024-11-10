"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[7195],{6447:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>c,contentTitle:()=>a,default:()=>h,frontMatter:()=>l,metadata:()=>s,toc:()=>o});const s=JSON.parse('{"id":"rules/changeset-id","title":"changeset-id","description":"Why?","source":"@site/docs/rules/changeset-id.md","sourceDirName":"rules","slug":"/rules/changeset-id","permalink":"/liquibase-linter/docs/rules/changeset-id","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{"title":"changeset-id"},"sidebar":"docs","previous":{"title":"changeset-author","permalink":"/liquibase-linter/docs/rules/changeset-author"},"next":{"title":"file-not-included","permalink":"/liquibase-linter/docs/rules/file-not-included"}}');var i=t(4848),r=t(8453);const l={title:"changeset-id"},a=void 0,c={},o=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2},{value:"Example Usage",id:"example-usage",level:2}];function d(e){const n={code:"code",h2:"h2",li:"li",p:"p",pre:"pre",ul:"ul",...(0,r.R)(),...e.components};return(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(n.h2,{id:"why",children:"Why?"}),"\n",(0,i.jsx)(n.p,{children:"You might want to enforce a naming pattern for the id of your changeset."}),"\n",(0,i.jsxs)(n.p,{children:["The ",(0,i.jsx)(n.code,{children:"changeset-id"})," will fail if the given regex does not match against the ",(0,i.jsx)(n.code,{children:"id"})," of the changeset."]}),"\n",(0,i.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"pattern"})," - (regex, as string) regular expression that the ",(0,i.jsx)(n.code,{children:"id"})," of any ",(0,i.jsx)(n.code,{children:"changeset"})," must adhere to"]}),"\n"]}),"\n",(0,i.jsx)(n.h2,{id:"example-usage",children:"Example Usage"}),"\n",(0,i.jsx)(n.pre,{children:(0,i.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "changeset-id": {\n            "enabled": true,\n            "pattern": "^\\\\d{8}_[a-z_]+$"\n        }\n    }\n}\n'})})]})}function h(e={}){const{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,i.jsx)(n,{...e,children:(0,i.jsx)(d,{...e})}):d(e)}},8453:(e,n,t)=>{t.d(n,{R:()=>l,x:()=>a});var s=t(6540);const i={},r=s.createContext(i);function l(e){const n=s.useContext(r);return s.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function a(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(i):e.components||i:l(e.components),s.createElement(r.Provider,{value:n},e.children)}}}]);